package org.example.tennisscoreboard.domain.model;

import lombok.Getter;

public class Score {


    @Getter
    private Point point;

    @Getter
    private Game game;

    @Getter
    private Set set;

    @Getter
    private Tiebreak tiebreakPoint;

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
