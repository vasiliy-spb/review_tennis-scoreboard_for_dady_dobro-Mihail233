package org.example.tennisscoreboard.domain.model.score;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class Point {
    private static int INITIAL_POINTS = 0;

    public static final int AD = -1;
    public static final int LOVE = 0;
    public static final int ONE_POINT = 15;
    public static final int TWO_POINT = 30;
    public static final int THREE_POINT = 40;

    private static final ArrayList<Integer> ENABLE_POINT = new ArrayList<>(List.of(AD, LOVE, ONE_POINT, TWO_POINT, THREE_POINT));

    @Getter
    private int points;

    private Point() {};

    private Point(int points) {
        this.points = points;
    }

    private void setPoints(int points) {
        this.points = points;
    }

    protected static Point createPoint() {
        return new Point(INITIAL_POINTS);
    }

    protected static Point createSpecificPoint(int points) {
        validatePoints(points);
        return new Point(points);
    }

    private static void validatePoints(int points) {

        if (!ENABLE_POINT.contains(points)) {
            throw new IllegalArgumentException("Invalid points");
        }
    }

    protected void createAdvantageGame() {
        setPoints(AD);
    }

    protected void resetStandardPoints() {
        setPoints(LOVE);
    }

    protected void returnToThreePoint() {
        setPoints(THREE_POINT);
    }

    protected void awardPoints() {
        switch (points) {
            case (LOVE):
                setPoints(ONE_POINT);
                break;
            case (ONE_POINT):
                setPoints(TWO_POINT);
                break;
            case (TWO_POINT):
                setPoints(THREE_POINT);
                break;
            default:
                throw new IllegalArgumentException("Unexpected value");
        }
    }
}
