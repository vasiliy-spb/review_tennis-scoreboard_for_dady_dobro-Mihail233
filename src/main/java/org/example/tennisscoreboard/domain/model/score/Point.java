package org.example.tennisscoreboard.domain.model.score;

import lombok.Getter;

public class Point {
    private static int INITIAL_POINTS = 0;

    @Getter
    private int points;

    private Point() {};

    private Point(int points) {
        this.points = points;
    }

    protected void setPoints(int points) {
        this.points = points;
    }

    protected static Point createPoint() {
        return new Point(INITIAL_POINTS);
    }

    protected static Point createSpecificPoint(int points) {
        return new Point(points);
    }
}
