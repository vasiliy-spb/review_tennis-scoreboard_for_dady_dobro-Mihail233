package org.example.tennisscoreboard.domain.model;

import lombok.Getter;

public class Score {

    // TODO: Класс нарушает Принцип единой ответственности (SRP) и
        // забирает часть логики у каждого нижестоящего класса модели.
        // А также он предоставляет статические методы для создания
        // объектов с предустановленными параметрами для тестов.
        // За логику обработки счёта должны отвечать сами матч-сет-гейм:
            // - Матч запускает обработку очка у сета и если тот завершён, увеличивает счёт у себя
            // - Сет запускает обработку очка у гейма/тай-брейка и если тот завершён, увеличивает счёт у себя
        // Класс не имеет самостоятельной логики и оправданного назначения, поэтому не должен существовать.

    @Getter
    private Point point;

    @Getter
    private Game game;

    @Getter
    private Set set;

    @Getter
    private Tiebreak tiebreakPoint;

    // Если в классе есть хоть один конструктор, то конструктор по умолчанию
        // (публичный конструктор без аргументов) создан не будет,
        // поэтому не нужно объявлять его как private.
    private Score() {
    }

    private Score(Point point, Game game, Set set, Tiebreak tiebreakPoint) {
        this.point = point;
        this.game = game;
        this.set = set;
        this.tiebreakPoint = tiebreakPoint;
    }

    protected static Score createScore(String firstParticipantName, String secondParticipantName) {
        return new Score(
                Point.createPoint(firstParticipantName, secondParticipantName),
                Game.createGame(firstParticipantName, secondParticipantName),
                Set.createSet(firstParticipantName, secondParticipantName),
                Tiebreak.createTiebreak(firstParticipantName, secondParticipantName)
        );
    }

    // Удобство использования в тестах не является достаточной причиной для того, чтобы создавать такие методы.
        // Тесты должны писаться для существующего кода и подстраиваться под него, а не наоборот.
        // Этот метод стоит удалить.
    protected static Score createCustomScoreWithTiebreak(String firstParticipantName, String secondParticipantName,
                                                            int firstParticipantPoints, int secondParticipantPoints,
                                                            int firstParticipantGames, int secondParticipantGames,
                                                            int firstParticipantSets, int secondParticipantSets,
                                                            int firstParticipantTiebreakPoints, int secondParticipantTiebreakPoints
    ) {
        return new Score(
                Point.createCustomPoint(firstParticipantName, firstParticipantPoints, secondParticipantName, secondParticipantPoints),
                Game.createCustomGame(firstParticipantName, firstParticipantGames, secondParticipantName, secondParticipantGames),
                Set.createCustomSet(firstParticipantName, firstParticipantSets, secondParticipantName, secondParticipantSets),
                Tiebreak.createCustomTiebreak(firstParticipantName,firstParticipantTiebreakPoints, secondParticipantName, secondParticipantTiebreakPoints)
        );
    }

    // Удобство использования в тестах не является достаточной причиной для того, чтобы создавать такие методы.
        // Тесты должны писаться для существующего кода и подстраиваться под него, а не наоборот.
        // Этот метод стоит удалить.
    protected static Score createCustomScoreWithoutTiebreak(String firstParticipantName, String secondParticipantName,
                                                            int firstParticipantPoints, int secondParticipantPoints,
                                                            int firstParticipantGames, int secondParticipantGames,
                                                            int firstParticipantSets, int secondParticipantSets) {
        return new Score(
                Point.createCustomPoint(firstParticipantName, firstParticipantPoints, secondParticipantName, secondParticipantPoints),
                Game.createCustomGame(firstParticipantName, firstParticipantGames, secondParticipantName, secondParticipantGames),
                Set.createCustomSet(firstParticipantName, firstParticipantSets, secondParticipantName, secondParticipantSets),
                Tiebreak.createTiebreak(firstParticipantName, secondParticipantName)
        );
    }



    protected void awardPointByName(String name) {
        if (game.isTiebreak()) {
            tiebreakPoint.updatePointInTiebreak(name);
        } else {
            point.updatePointInStandardGame(name);
        }

        if (isEndedGame()) {
            game.updateGames(name);
        }

        if (isEndedSet()) {
            set.updateSets(name);
        }
    }

    private boolean isEndedGame() {
        return point.isReset() &&
                tiebreakPoint.isReset();
    }

    private boolean isEndedSet() {
        return point.isReset() && game.isReset();
    }
}
