package org.example.tennisscoreboard.domain.model.score;

import lombok.Getter;

public class Score {
    public static final int AD = -1;
    public static final int LOVE = 0;
    static final int ONE_POINT = 15;
    static final int TWO_POINT = 30;
    static final int THREE_POINT = 40;
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

    protected static Score createSpecificScore(int points, int games, int sets, int tiebreakPoints) {
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
        points.setPoints(AD);
    }

    protected void resetStandardPoints() {
        points.setPoints(LOVE);
    }

    protected void returnToThreePoint() {
        points.setPoints(THREE_POINT);
    }

    protected void resetGames() {
        games.setGames(LOVE);
    }

    protected void awardPoints() {
        switch (points.getPoints()) {
            case (LOVE):
                points.setPoints(ONE_POINT);
                break;
            case (ONE_POINT):
                points.setPoints(TWO_POINT);
                break;
            case (TWO_POINT):
                points.setPoints(THREE_POINT);
                break;
            default:
                throw new IllegalArgumentException("Unexpected value");
        }
    }

    protected void incrementGames() {
        games.incrementGames();
    }

    protected void incrementSets() {
        sets.incrementSets();
    }
}
