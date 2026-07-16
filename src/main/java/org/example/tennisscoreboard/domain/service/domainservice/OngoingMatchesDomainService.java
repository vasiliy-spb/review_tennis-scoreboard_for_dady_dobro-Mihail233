package org.example.tennisscoreboard.domain.service.domainservice;

import lombok.RequiredArgsConstructor;
import org.example.tennisscoreboard.domain.model.score.TennisMatch;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class OngoingMatchesDomainService {
    private final Map<UUID, TennisMatch> ongoingMatches = new HashMap<>();

    public void addNewMatch(TennisMatch tennisMatch) {
        ongoingMatches.put(tennisMatch.getUuid(), tennisMatch);
    }

    public TennisMatch getOngoingMatch(String uuid) {
        TennisMatch tennisMatch = ongoingMatches.get(UUID.fromString(uuid));
        if (tennisMatch == null) {
            throw new RuntimeException("Ongoing match not found");
        }

        return tennisMatch;
    }
//
    public void saveScore(String uuid, TennisMatch tennisMatch) {
        ongoingMatches.put(UUID.fromString(uuid), tennisMatch);
    }
//
//    public void deleteFinishedMatch(String uuid, PlayerDTO winnerDTO) {
//        Player winner = playerMapper.toObject(winnerDTO);
//        if (winner != null) {
//            ongoingMatches.remove(UUID.fromString(uuid));
//        }
//    }
}