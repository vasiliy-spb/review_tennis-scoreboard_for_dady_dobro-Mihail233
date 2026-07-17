package org.example.tennisscoreboard.domain.service;

import lombok.RequiredArgsConstructor;
import org.example.tennisscoreboard.domain.model.score.TennisMatch;
import org.example.tennisscoreboard.exception.OngoingMatchNotFoundException;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class OngoingMatchesDomainService {
    private final Map<UUID, TennisMatch> ongoingMatches = new ConcurrentHashMap<>();

    public void addNewMatch(TennisMatch tennisMatch) {
        ongoingMatches.put(tennisMatch.getUuid(), tennisMatch);
    }

    public TennisMatch getOngoingMatch(String uuid) {
        TennisMatch tennisMatch = ongoingMatches.get(UUID.fromString(uuid));
        if (tennisMatch == null) {
            throw new OngoingMatchNotFoundException("Текущий матч не найден");
        }

        return tennisMatch;
    }

    public void deleteFinishedMatch(String uuid) {
        TennisMatch tennisMatch = getOngoingMatch(uuid);
        if (tennisMatch.getWinner() != null) {
            ongoingMatches.remove(UUID.fromString(uuid));
        }
    }
}