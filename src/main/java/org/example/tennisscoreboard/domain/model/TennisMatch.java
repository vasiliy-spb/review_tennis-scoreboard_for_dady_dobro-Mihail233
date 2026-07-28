package org.example.tennisscoreboard.domain.model;

import lombok.Getter;

import java.util.Objects;
import java.util.UUID;

public class TennisMatch {
    private static final int SETS_TO_WIN_MATCH = 2;

    @Getter
    private UUID uuid;

    @Getter
    private Participants participants;

    @Getter
    private Score score;

    // TODO: Хранение поля Participant winner вынуждает следить не только за состоянием счёта, но и за этим полем.
        // Это нарушает Принцип Единого источника истины (см. файл "ssot-principle.md" в этом же пакете).
        // Победителя в матче можно вычислять по счёту.
    // Вместо простого геттера и метода void findWinner() лучше иметь метод Optional<Participant> getWinner(),
        // который никогда не вернёт null и будет вычислять победителя "на лету".
    @Getter
    private Participant winner;

    // Если в классе есть хоть один конструктор, то конструктор по умолчанию
        // (публичный конструктор без аргументов) создан не будет,
        // поэтому не нужно объявлять его как private.
    private TennisMatch() {
    }

    private TennisMatch(Participants participants, Score score) {

        // Стоит удалять закомментированный код перед тем, как выполнять коммит
        //this.uuid = UUID.fromString("f609a413-255a-4eae-925a-dedddd67e470");

        // Матч не должен сам генерировать свой ID — это ответственность класса, который его создаёт или сохраняет в БД/хранилище.
        this.uuid = UUID.randomUUID();
        this.participants = participants;
        this.score = score;
    }

    public static TennisMatch createMatch(Participants participants) {
        String firstParticipantName = participants.firstParticipant().getName();
        String secondParticipantName = participants.secondParticipant().getName();

        Score score = Score.createScore(firstParticipantName, secondParticipantName);
        return new TennisMatch(participants, score);
    }

    // Удобство использования в тестах не является достаточной причиной для того, чтобы создавать такие методы.
        // Тесты должны писаться для существующего кода и подстраиваться под него, а не наоборот.
        // Этот метод стоит удалить.
    public static TennisMatch createCustomMatchWithTiebreak(Participants participants,
                                                            int firstParticipantPoints, int secondParticipantPoints,
                                                            int firstParticipantGames, int secondParticipantGames,
                                                            int firstParticipantSets, int secondParticipantSets,
                                                            int firstParticipantTiebreakPoints, int secondParticipantTiebreakPoints) {
        String firstParticipantName = participants.firstParticipant().getName();
        String secondParticipantName = participants.secondParticipant().getName();

        Score score = Score.createCustomScoreWithTiebreak(
                firstParticipantName, secondParticipantName,
                firstParticipantPoints, secondParticipantPoints,
                firstParticipantGames, secondParticipantGames,
                firstParticipantSets, secondParticipantSets,
                firstParticipantTiebreakPoints, secondParticipantTiebreakPoints
        );
        return new TennisMatch(participants, score);
    }

    // Удобство использования в тестах не является достаточной причиной для того, чтобы создавать такие методы.
        // Тесты должны писаться для существующего кода и подстраиваться под него, а не наоборот.
        // Этот метод стоит удалить.
    public static TennisMatch createCustomMatchWithoutTiebreak(Participants participants,
                                                               int firstParticipantPoints, int secondParticipantPoints,
                                                               int firstParticipantGames, int secondParticipantGames,
                                                               int firstParticipantSets, int secondParticipantSets
    ) {
        String firstParticipantName = participants.firstParticipant().getName();
        String secondParticipantName = participants.secondParticipant().getName();

        Score score = Score.createCustomScoreWithoutTiebreak
                (firstParticipantName, secondParticipantName,
                        firstParticipantPoints, secondParticipantPoints,
                        firstParticipantGames, secondParticipantGames,
                        firstParticipantSets, secondParticipantSets
                );
        return new TennisMatch(participants, score);
    }

    // Лучше назвать pointWonBy(name)
    // Здесь аргумент метода назван winnerName, а в других классах просто name — лучше придерживаться единообразия
    // TODO: Определение победителя в этом методе является "побочным эффектом".
        // Ничто не мешает в уже завершённом матче вызвать несколько раз метод awardPoint для проигравшего
        // и тем самым изменить победителя.
        // Метод findWinner() (который после рефакторинга преобразуется в Optional<Participant> getWinner())
        // должен запускаться отдельно.
    public void awardPoint(String winnerName) {
        // TODO: Нет проверки на то, что матч не завершён.
            // Попытка начислить очко в уже завершённом матче — это не нормальная ситуация и
            // должна приводить к исключению.

        score.awardPointByName(winnerName);
        findWinner();
    }

    private void findWinner() {
        Set set = score.getSet();

        set.getSets().forEach((key, value) -> {
            if (value.equals(SETS_TO_WIN_MATCH)) {
                winner = (Objects.equals(participants.firstParticipant().getName(), key)) ? participants.firstParticipant() : participants.secondParticipant();
            }
        });
    }
}
