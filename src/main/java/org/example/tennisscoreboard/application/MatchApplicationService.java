package org.example.tennisscoreboard.application;

import lombok.RequiredArgsConstructor;
import org.example.tennisscoreboard.domain.model.TennisMatch;
import org.example.tennisscoreboard.domain.model.Participants;
import org.example.tennisscoreboard.domain.service.OngoingMatchesDomainService;
import org.example.tennisscoreboard.dto.*;
import org.example.tennisscoreboard.mapper.TennisMatchMapper;
import org.example.tennisscoreboard.service.FinishedMatchesFindingService;
import org.example.tennisscoreboard.service.FinishedMatchesPersistenceService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MatchApplicationService {
    private final PlayerApplicationService playerApplicationService;
    private final OngoingMatchesDomainService ongoingMatchesDomainService;
    private final FinishedMatchesPersistenceService finishedMatchesPersistenceService;
    private final FinishedMatchesFindingService finishedMatchesFindingService;

    private final TennisMatchMapper tennisMatchMapper;

    public RegisteredMatchResponse createNewMatch(MatchCreationRequest matchCreationRequest) {
        Participants participants = playerApplicationService.findParticipants(matchCreationRequest.firstPlayerName(), matchCreationRequest.secondPlayerName());
        TennisMatch tennisMatch = TennisMatch.createMatch(participants);

        ongoingMatchesDomainService.addNewMatch(tennisMatch);
        return new RegisteredMatchResponse(tennisMatch.getUuid());
    }

    public TennisMatchResponse getGeneralScore(String uuid) {
        TennisMatch tennisMatch = ongoingMatchesDomainService.getOngoingMatch(uuid);
        return tennisMatchMapper.toDTO(tennisMatch);
    }

    public TennisMatchResponse awardPointAndGetGeneralScore(String uuid, PointAwardingRequest pointAwardingRequest) {

        TennisMatch tennisMatch = ongoingMatchesDomainService.getOngoingMatch(uuid);
        String winnerName = pointAwardingRequest.name();
        tennisMatch.awardPoint(winnerName);

        ongoingMatchesDomainService.deleteFinishedMatch(uuid);
        finishedMatchesPersistenceService.addFinishedMatch(tennisMatch);

        return tennisMatchMapper.toDTO(tennisMatch);
    }

    public FinishedMatchesResponse getFinishedMatches(String pageFromUser, String playerName) {
        if (playerName == null) {
            return finishedMatchesFindingService.findFinishedMatches(pageFromUser);
        } else {
            return finishedMatchesFindingService.findFinishedMatchesByPlayer(playerName, pageFromUser);
        }
    }
}
