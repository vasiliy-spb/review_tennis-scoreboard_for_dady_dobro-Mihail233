package org.example.tennisscoreboard.domain.model.score;

import lombok.Getter;

public class Score {
    @Getter
    private Point points;

    @Getter
    private Game games;

    @Getter
    private Set sets;

    @Getter
    private Tiebreak tiebreakPoints;

    private Score() {
    }

    private Score(Point points, Game games, Set sets, Tiebreak tiebreakPoints) {
        this.points = points;
        this.games = games;
        this.sets = sets;
        this.tiebreakPoints = tiebreakPoints;
    }

    //идея aggregator root
    protected static Score createDefaultScore() {
        return new Score(Point.createPoint(), Game.createGame(), Set.createSet(), null);
    }

    protected static Score createSpecificScoreWithTiebreak(int points, int games, int sets, int tiebreakPoints) {
        return new Score(Point.createSpecificPoint(points), Game.createSpecificGame(games), Set.createSpecificSet(sets), Tiebreak.createSpecificTiebreak(tiebreakPoints));
    }

    protected void createTiebreak() {
        tiebreakPoints = Tiebreak.createTiebreak();
    }

    protected void incrementPointInTiebreak() {
        tiebreakPoints.incrementPoint();
    }

    protected void deleteTiebreak() {
        tiebreakPoints = null;
    }

    protected void createAdvantageGame() {
        points.createAdvantageGame();
    }

    protected void resetStandardPoints() {
        points.resetStandardPoints();
    }

    protected void returnToThreePoint() {
        points.returnToThreePoint();
    }

    protected void resetGames() {
        games.resetGames();
    }

    protected void awardPoints() {
        points.awardPoints();
    }

    protected void incrementGames() {
        games.incrementGames();
    }

    protected void incrementSets() {
        sets.incrementSets();
    }
}
