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

    // TODO: Нет интерфейса для этого класса. (см. файл "service.md" в этом же пакете)

    // Всю работу с завершёнными матчами можно реализовать в одном сервисе.
        // Иначе эта ответственность получается слишком раздробленной.

    // Размер страницы и номер по умолчанию более уместно хранить в контроллере, так как в идеале он должен приходить с фронтенда.
        // А сервис должен принимать это значение в качестве аргумента в методы.

    // Методы этого сервиса должны принимать номер страницы в int, а не парсить его из строки.

    public final static int STARTED_PAGE = 1;
    public final static int DEFAULT_PAGE_SIZE = 10;

    // Лучше назвать NUMBER_INDEX_DIFFERENCE
    private final static int TO_INDEX = 1;

    // EMPTY_PAGE_NUMBER читалось бы лучше
    private final static int PAGES_NOT_FOUND = 0;

    // Просто ZERO читалось бы лучше. Или можно вообще не выносить 0 в константу в этом случае.
    private final static int RECORDS_NOT_FOUND = 0;

    private final ExtendedDAO<Match, List<Match>> extendedDAO;

    private final MatchMapper matchMapper;

    // Можно просто find
    // Номер страницы стоит парсить как можно ближе ко входу этих данных в приложение (в контроллере),
        // а в метод сервиса принимать уже int.
    public FinishedMatchesResponse findFinishedMatches(String pageFromUser) {
        int matchCount = extendedDAO.countAllMatches();
        FinderRecords finderRecords = extendedDAO::find;

        return findBatchOfFinishedMatches(matchCount, pageFromUser, finderRecords);
    }

    // Можно просто findWithPlayerName
    // Номер страницы стоит парсить как можно ближе ко входу этих данных в приложение (в контроллере),
        // а в метод сервиса принимать уже int.
    public FinishedMatchesResponse findFinishedMatchesByPlayer(String playerName, String pageFromUser) {
        int recordCount = extendedDAO.countAllMatchesByPlayerName(playerName);
        FinderRecords finderAllRecordsOneOfPlayer = (offset) -> extendedDAO.findMatchesByNameAndPage(playerName, offset);

        return findBatchOfFinishedMatches(recordCount, pageFromUser, finderAllRecordsOneOfPlayer);
    }

    private FinishedMatchesResponse findBatchOfFinishedMatches(int recordCount, String pageFromUser, FinderRecords finderRecords) {
        int page = convertAndValidatePage(pageFromUser);

        if (recordCount == RECORDS_NOT_FOUND) {

            // Раз STARTED_PAGE == 1, то когда записей нет, стоит возвращать её, а не PAGES_NOT_FOUND (0)
            return new FinishedMatchesResponse(new ArrayList<>(), page, PAGES_NOT_FOUND);

        } else { // Если из блока if происходит return, то следующий код можно писать без else
            int pageIndex = page - TO_INDEX;
            int offset = pageIndex * DEFAULT_PAGE_SIZE;

            // TODO: Не нужно здесь конвертировать offset в строку, чтобы потом в DAO парсить обратно
            List<Match> matches = finderRecords.find(String.valueOf(offset));
            int totalPages = findTotalPages(recordCount);

            List<MatchResponse> matchDTOs = matchMapper.toDTO(matches);
            return new FinishedMatchesResponse(matchDTOs, page, totalPages);
        }
    }

    // Номер страницы стоит парсить как можно ближе ко входу этих данных в приложение (в контроллере),
        // а в методы сервиса принимать уже int.
    private int convertAndValidatePage(String pageFromUser) {
        try {
            int page = pageFromUser == null ? STARTED_PAGE : Integer.parseInt(pageFromUser);
            validatePage(page);
            return page;
        } catch (Exception e) { // Достаточно ловить здесь более узкое NumberFormatException

            // Сообщение об ошибке "Передан не номер страницы" не подходит для случая page < 1
            // Текст сообщения в исключениях принято писать на английском языке.
            throw new InvalidPageException("Передан не номер страницы");
        }
    }

    private void validatePage(int page) {
        if (page < STARTED_PAGE) {

            // Не указано сообщение в исключении — это осложнить отладку
            // В проекте есть InvalidPageException, которое здесь было бы более уместным
            throw new RuntimeException();
        }
    }

    private int findTotalPages(int recordCount) {
        return (int) Math.ceil((double) recordCount / DEFAULT_PAGE_SIZE);
    }
}

