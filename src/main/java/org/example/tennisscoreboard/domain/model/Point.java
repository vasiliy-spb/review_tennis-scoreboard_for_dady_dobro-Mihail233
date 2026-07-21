package org.example.tennisscoreboard.domain.model;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Point {
    private static final int INITIAL_POINTS = 0;

    public static final int AD = -1;
    public static final int LOVE = 0;
    public static final int ONE_POINT = 15;
    public static final int TWO_POINT = 30;
    public static final int THREE_POINT = 40;

    @Getter
    private Map<String, Integer> points;

    private Point() {
    }

    private Point(String firstParticipantName, int firstParticipantPoints, String secondParticipantName, int secondParticipantPoints) {
        initPoints(firstParticipantName, firstParticipantPoints, secondParticipantName, secondParticipantPoints);
    }

    private void initPoints(String firstParticipantName, int firstParticipantPoints, String secondParticipantName, int secondParticipantPoints) {
        points = new HashMap<>();
        points.put(firstParticipantName, firstParticipantPoints);
        points.put(secondParticipantName, secondParticipantPoints);
    }

    protected static Point createPoint(String firstParticipantName, String secondParticipantName) {
        return new Point(firstParticipantName, INITIAL_POINTS, secondParticipantName, INITIAL_POINTS);
    }

    protected void updatePointInStandardGame(String name) {
        if (isDeuce()) {
            startAdvantageGame(name);
        } else if (isAdvantage()) {
            returnToDeuceOrFinishAdvantageGame(name);
        } else {
            continueOrFinishStandardGame(name);
        }
    }

    private boolean isDeuce() {
        return areAllPointsEqualTo(THREE_POINT);
    }

    private boolean areAllPointsEqualTo(int point) {
        return points.values().stream()
                .allMatch(points -> Objects.equals(points, point));
    }

    private void startAdvantageGame(String name) {
        points.put(name, AD);
    }

    private boolean isAdvantage() {
        return points.values().stream()
                .anyMatch(points -> Objects.equals(points, AD));
    }

    private void returnToDeuceOrFinishAdvantageGame(String name) {
        Integer winnerPoints = points.get(name);

        if (winnerPoints == AD) {
            finishGame();
        } else {
            returnToDeuce(name);
        }
    }

    private void finishGame() {
        points.forEach((key, _) -> points.put(key, LOVE));
    }

    private void returnToDeuce(String name) {
        points.forEach((key, _) -> {
            if (!Objects.equals(key, name)) {
                points.put(key, THREE_POINT);
            }
        });
    }

    private void continueOrFinishStandardGame(String name) {
        Integer winnerPoints = points.get(name);

        if (winnerPoints == THREE_POINT) {
            finishGame();
        } else {
            awardPoints(name);
        }
    }

    private void awardPoints(String name) {
        Integer winnerPoints = points.get(name);
        switch (winnerPoints) {
            case (LOVE):
                points.put(name, ONE_POINT);
                break;
            case (ONE_POINT):
                points.put(name, TWO_POINT);
                break;
            case (TWO_POINT):
                points.put(name, THREE_POINT);
                break;
            default:
                throw new IllegalArgumentException("Unexpected value");
        }
    }

    protected boolean isReset() {
        return areAllPointsEqualTo(LOVE);
    }
}
