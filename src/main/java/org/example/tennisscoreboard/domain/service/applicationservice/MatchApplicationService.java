package org.example.tennisscoreboard.domain.service.applicationservice;

import lombok.RequiredArgsConstructor;
import org.example.tennisscoreboard.domain.model.Participants;
import org.example.tennisscoreboard.domain.model.score.TennisMatch;
import org.example.tennisscoreboard.domain.service.domainservice.OngoingMatchesDomainService;
import org.example.tennisscoreboard.dto.MatchCreationRequest;
import org.example.tennisscoreboard.dto.PointAwardingRequest;
import org.example.tennisscoreboard.dto.RegisteredMatchDTO;
import org.example.tennisscoreboard.dto.TennisMatchResponse;
import org.example.tennisscoreboard.mapper.model.TennisMatchMapper;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MatchApplicationService {
    private final PlayerApplicationService playerApplicationService;
    private final OngoingMatchesDomainService ongoingMatchesDomainService;

    private final TennisMatchMapper tennisMatchMapper;

    public RegisteredMatchDTO createNewMatch(MatchCreationRequest matchCreationRequest) {
        Participants participants = playerApplicationService.findParticipants(matchCreationRequest.firstPlayerName(), matchCreationRequest.secondPlayerName());
        TennisMatch tennisMatch = TennisMatch.createMatch(participants);

        ongoingMatchesDomainService.addNewMatch(tennisMatch);
        return new RegisteredMatchDTO(tennisMatch.getUuid());
    }

    public TennisMatchResponse getGeneralScore(String uuid) {
        TennisMatch tennisMatch = ongoingMatchesDomainService.getOngoingMatch(uuid);
        return tennisMatchMapper.toDTO(tennisMatch);
    }

    public TennisMatchResponse awardPointAndGetGeneralScore(String uuid, PointAwardingRequest pointAwardingRequest) {

        TennisMatch tennisMatch = ongoingMatchesDomainService.getOngoingMatch(uuid);
        String winnerName = pointAwardingRequest.name();
        tennisMatch.awardPoint(winnerName);

        return tennisMatchMapper.toDTO(tennisMatch);
    }
}
