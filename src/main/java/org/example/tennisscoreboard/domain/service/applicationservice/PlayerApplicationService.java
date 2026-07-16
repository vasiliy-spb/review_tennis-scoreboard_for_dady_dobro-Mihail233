package org.example.tennisscoreboard.domain.service.applicationservice;

import lombok.RequiredArgsConstructor;
import org.example.tennisscoreboard.common.dao.DAO;
import org.example.tennisscoreboard.domain.model.Participant;
import org.example.tennisscoreboard.domain.model.Participants;
import org.example.tennisscoreboard.entity.Player;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PlayerApplicationService {

    private final DAO<Player, Player> h2PlayerDAO;

    //если 2 пользователя исп app параллельно,
    //у первого при select будет null, второй пользователь вставит в этот момент player,
    //первый попытается вставить и у него будет exception

    public Participants findParticipants(String firstPlayerName, String secondPlayerName) {

        Player firstPlayer = new Player(firstPlayerName);
        Player secondPlayer = new Player(secondPlayerName);

        h2PlayerDAO.insert(firstPlayer);
        h2PlayerDAO.insert(secondPlayer);

        firstPlayer = h2PlayerDAO.find(firstPlayerName);
        secondPlayer = h2PlayerDAO.find(secondPlayerName);

        Participant firstParticipant = Participant.createParticipant(firstPlayer.getId(), firstPlayer.getName());
        Participant secondParticipant = Participant.createParticipant(secondPlayer.getId(), secondPlayer.getName());

        return new Participants(firstParticipant, secondParticipant);
    }

}
