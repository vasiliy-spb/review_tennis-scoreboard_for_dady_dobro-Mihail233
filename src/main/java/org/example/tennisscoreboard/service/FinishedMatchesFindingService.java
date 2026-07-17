package org.example.tennisscoreboard.service;

import lombok.RequiredArgsConstructor;
import org.example.tennisscoreboard.common.dao.ExtendedDAO;
import org.example.tennisscoreboard.common.dao.functional.FinderRecords;
import org.example.tennisscoreboard.dto.FinishedMatchesResponse;
import org.example.tennisscoreboard.dto.MatchResponse;
import org.example.tennisscoreboard.entity.Match;
import org.example.tennisscoreboard.exception.InvalidPageException;
import org.example.tennisscoreboard.mapper.MatchMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class FinishedMatchesFindingService {
    public final static int STARTED_PAGE = 1;
    public final static int DEFAULT_PAGE_SIZE = 10;

    private final static int TO_INDEX = 1;

    private final static int PAGES_NOT_FOUND = 0;
    private final static int RECORDS_NOT_FOUND = 0;

    private final ExtendedDAO<Match, List<Match>> extendedDAO;

    private final MatchMapper matchMapper;


    public FinishedMatchesResponse findAllFinishedMatches(String pageFromUser) {
        int matchCount = extendedDAO.countAllMatches();
        FinderRecords finderRecords = extendedDAO::find;

        return findBatchOfFinishedMatches(matchCount, pageFromUser, finderRecords);
    }

    public FinishedMatchesResponse findAllFinishedMatchesCertainPlayer(String playerName, String pageFromUser) {
        int recordCount = extendedDAO.countAllMatchesByPlayerName(playerName);
        FinderRecords finderAllRecordsOneOfPlayer = (offset) -> extendedDAO.findMatchesByNameAndPage(playerName, offset);

        return findBatchOfFinishedMatches(recordCount, pageFromUser, finderAllRecordsOneOfPlayer);
    }

    private FinishedMatchesResponse findBatchOfFinishedMatches(int recordCount, String pageFromUser, FinderRecords finderRecords) {
        int page = convertAndValidatePage(pageFromUser);

        if (recordCount == RECORDS_NOT_FOUND) {
            return new FinishedMatchesResponse(new ArrayList<>(), page, PAGES_NOT_FOUND);

        } else {
            int pageIndex = page - TO_INDEX;
            int offset = pageIndex * DEFAULT_PAGE_SIZE;

            List<Match> matches = finderRecords.find(String.valueOf(offset));
            int totalPages = findTotalPages(recordCount);

            List<MatchResponse> matchDTOs = matchMapper.toDTO(matches);
            return new FinishedMatchesResponse(matchDTOs, page, totalPages);
        }
    }

    private int convertAndValidatePage(String pageFromUser) {
        try {
            int page = pageFromUser == null ? STARTED_PAGE : Integer.parseInt(pageFromUser);
            validatePage(page);
            return page;
        } catch (Exception e) {
            throw new InvalidPageException("Передан не номер страницы");
        }
    }

    private void validatePage(int page) {
        if (page < STARTED_PAGE) {
            throw new RuntimeException();
        }
    }

    private int findTotalPages(int recordCount) {
        return (int) Math.ceil((double) recordCount / DEFAULT_PAGE_SIZE);
    }
}

