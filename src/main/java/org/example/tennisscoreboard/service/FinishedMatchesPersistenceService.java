package org.example.tennisscoreboard.service;

import lombok.RequiredArgsConstructor;
import org.example.tennisscoreboard.common.dao.DAO;
import org.example.tennisscoreboard.common.dao.ExtendedDAO;
import org.example.tennisscoreboard.domain.model.TennisMatch;
import org.example.tennisscoreboard.domain.model.Participants;
import org.example.tennisscoreboard.entity.Match;
import org.example.tennisscoreboard.entity.Player;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FinishedMatchesPersistenceService {
    private final ExtendedDAO<Match, List<Match>> extendedDAO;
    private final DAO<Player, Player> dao;

    public void addFinishedMatch(TennisMatch tennisMatch) {
        if (tennisMatch.getWinner() != null) {
            Participants participants = tennisMatch.getParticipants();

            Player firstPlayer = dao.find(participants.firstParticipant().getName());
            Player secondPlayer = dao.find(participants.secondParticipant().getName());
            Player winner = findWinnerFromPlayers(tennisMatch, firstPlayer, secondPlayer);

            extendedDAO.insert(new Match(firstPlayer, secondPlayer, winner));
        }
    }

    private Player findWinnerFromPlayers(TennisMatch tennisMatch, Player firstPlayer, Player secondPlayer) {
        return tennisMatch.getWinner().getId().equals(firstPlayer.getId()) ? firstPlayer : secondPlayer;
    }
}
