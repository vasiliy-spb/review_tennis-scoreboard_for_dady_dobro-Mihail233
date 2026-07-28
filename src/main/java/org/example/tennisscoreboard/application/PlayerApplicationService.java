package org.example.tennisscoreboard.application;

import lombok.RequiredArgsConstructor;
import org.example.tennisscoreboard.common.dao.DAO;
import org.example.tennisscoreboard.domain.model.Participant;
import org.example.tennisscoreboard.domain.model.Participants;
import org.example.tennisscoreboard.entity.Player;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PlayerApplicationService {

    // Не понятна роль слова Application в названии класса. Можно просто PlayerService.

    // Класс стоит перенести в пакет service к другим сервисам.

    // TODO: Если потребуется дописать функционал для игры пара на пару, то игроков станет 4
        // и придётся переписывать код этого класса, хотя логика сохранения игроков не изменится.
        // Нужно, чтобы метод, сохраняющий игрока в БД сохранял только одного игрока,
        // а клиентский код пусть вызывает для нужного количества игроков.

    // TODO: Нет интерфейса для этого класса. (см. файл "application.md" в этом же пакете)

    private final DAO<Player, Player> dao;

    // Стоит удалять комментарии (вроде тех, что указаны в следующих 3-х строках) из кода перед тем, как выполнять коммит
    //если 2 пользователя исп app параллельно,
    //у первого при select будет null, второй пользователь вставит в этот момент player,
    //первый попытается вставить и у него будет exception

    // TODO: Название findParticipants вводит в заблуждение. Метод не ищет (не должен искать) игроков, а создаёт и сохраняет их в БД.
    // TODO: Создание обоих игроков должно происходить в одной транзакции, которая будет откатываться,
        // если хотя бы один игрок не будет создан. То есть транзакция должна оборачивать метод,
        // который вызывает findParticipants, чтобы оба его вызова были в одной транзакции.
    public Participants findParticipants(String firstPlayerName, String secondPlayerName) {

        Player firstPlayer = new Player(firstPlayerName);
        Player secondPlayer = new Player(secondPlayerName);

        dao.insert(firstPlayer);
        dao.insert(secondPlayer);

        // При использовании persist() в dao.insert() после сохранения объектов firstPlayer и secondPlayer ID в них
            // подставится автоматически и можно будет избавиться от этих лишних запросов на поиск.
        firstPlayer = dao.find(firstPlayerName);
        secondPlayer = dao.find(secondPlayerName);

        // Логику преобразования Player —> Participant можно вынести в маппер
        Participant firstParticipant = Participant.createParticipant(firstPlayer.getId(), firstPlayer.getName());
        Participant secondParticipant = Participant.createParticipant(secondPlayer.getId(), secondPlayer.getName());

        return new Participants(firstParticipant, secondParticipant);
    }

}
