package org.example.tennisscoreboard.domain.model.score;

import lombok.Getter;

public class Tiebreak {
    private static int INITIAL_TIEBREAK_POINTS = 0;
    private static int MIN_POINTS = 0;
    @Getter
    private int points;

    private Tiebreak() {};

    private Tiebreak(int points) {
        this.points = points;
    }


    protected void incrementPoint() {
        points++;
    }

    protected static Tiebreak createTiebreak() {
        return new Tiebreak(INITIAL_TIEBREAK_POINTS);
    }

    protected static Tiebreak createSpecificTiebreak(int points) {
        validateTiebreak(points);
        return new Tiebreak(points);
    }

    private static void validateTiebreak(int points) {
        if (points < MIN_POINTS) {
            throw new IllegalArgumentException("Invalid tiebreak points");
        }
    }
}
