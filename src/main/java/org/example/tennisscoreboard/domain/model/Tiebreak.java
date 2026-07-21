package org.example.tennisscoreboard.domain.model;

import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.example.tennisscoreboard.domain.model.Game.MINIMUM_POINT_MARGIN_IN_TIEBREAK;

public class Tiebreak {
    private final static int INITIAL_TIEBREAK_POINTS = 0;
    private static final Integer MINIMUM_POINTS_IN_TIEBREAK = 7;

    @Getter
    private Map<String, Integer> tiebreakPoints;

    private Tiebreak() {}

    private Tiebreak(String firstParticipantName, Integer firstParticipantPoints, String secondParticipantName, Integer secondParticipantPoints) {
        initTiebreakPoints(firstParticipantName, firstParticipantPoints, secondParticipantName, secondParticipantPoints);
    }

    private void initTiebreakPoints(String firstParticipantName, Integer firstParticipantPoints, String secondParticipantName, Integer secondParticipantPoints) {
        tiebreakPoints = new HashMap<>();
        tiebreakPoints.put(firstParticipantName, firstParticipantPoints);
        tiebreakPoints.put(secondParticipantName, secondParticipantPoints);
    }

    protected static Tiebreak createTiebreak(String firstParticipantName, String secondParticipantName) {
        return new Tiebreak(firstParticipantName, null, secondParticipantName, null);
    }

    protected void updatePointInTiebreak(String name) {
        if (isReset()) {
            startTiebreak();
        }

        incrementPointInTiebreak(name);

        if (isTiebreakFinished()) {
            finishTiebreak();
        }
    }

    protected boolean isReset() {
        return tiebreakPoints.values().stream()
                .allMatch(points -> Objects.equals(points, null));
    }

    private void startTiebreak() {
        tiebreakPoints.forEach((key, _) -> tiebreakPoints.put(key, INITIAL_TIEBREAK_POINTS));
    }

    private void incrementPointInTiebreak(String name) {
        tiebreakPoints.compute(name, (_, tiebreakPoint) -> tiebreakPoint + 1);
    }

    private boolean isTiebreakFinished() {
        int highestPoints = Collections.max(tiebreakPoints.values());
        int lowestPoints = Collections.min(tiebreakPoints.values());

        return highestPoints >= MINIMUM_POINTS_IN_TIEBREAK && (highestPoints - lowestPoints >= MINIMUM_POINT_MARGIN_IN_TIEBREAK);
    }

    private void finishTiebreak() {
        tiebreakPoints.forEach((key, _) -> tiebreakPoints.put(key, null));
    }
}
