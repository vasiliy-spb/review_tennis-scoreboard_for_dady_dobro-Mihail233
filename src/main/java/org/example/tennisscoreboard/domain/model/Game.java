package org.example.tennisscoreboard.domain.model;

import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.example.tennisscoreboard.domain.model.Point.LOVE;

public class Game {
    private static final int MIN_GAMES = 0;
    private static final int MAX_GAMES = 7;

    public static final Integer MINIMUM_GAMES_IN_SET = 6;
    public static final Integer MINIMUM_POINT_MARGIN_IN_TIEBREAK = 2;

    private static final int INITIAL_GAMES = 0;

    @Getter
    private Map<String, Integer> games;

    private Game() {
    }

    private Game(String firstParticipantName, int firstParticipantGames, String secondParticipantName, int secondParticipantGames) {
        initGames(firstParticipantName, firstParticipantGames, secondParticipantName, secondParticipantGames);
    }

    private void initGames(String firstParticipantName, int firstParticipantGames, String secondParticipantName, int secondParticipantGames) {
        games = new HashMap<>();
        games.put(firstParticipantName, firstParticipantGames);
        games.put(secondParticipantName, secondParticipantGames);
    }

    protected static Game createGame(String firstParticipantName, String secondParticipantName) {
        return new Game(firstParticipantName, INITIAL_GAMES, secondParticipantName, INITIAL_GAMES);
    }

    protected static Game createCustomGame(String firstParticipantName, int firstParticipantGames, String secondParticipantName,
                                           int secondParticipantGames) {
        validateGames(firstParticipantGames);
        validateGames(secondParticipantGames);
        return new Game(firstParticipantName, firstParticipantGames, secondParticipantName, secondParticipantGames);
    }

    private static void validateGames(int games) {
        if (games > MAX_GAMES || games < MIN_GAMES) {
            throw new IllegalArgumentException("Invalid games");
        }
    }

    protected boolean isTiebreak() {
        return areAllGamesEqualTo(MINIMUM_GAMES_IN_SET);
    }

    private boolean areAllGamesEqualTo(int game) {
        return games.values().stream()
                .allMatch(points -> Objects.equals(points, game));
    }

    protected void updateGames(String name) {
        incrementGames(name);

        if (isWinningSet()) {
            finishSet();
        }
    }

    protected void incrementGames(String name) {
        games.compute(name, (_, game) -> game + 1);
    }

    boolean isWinningSet() {

        int highestGames = Collections.max(games.values());
        int lowestGames = Collections.min(games.values());

        boolean isDefaultWinningSet = highestGames >= MINIMUM_GAMES_IN_SET && (highestGames - lowestGames >= MINIMUM_POINT_MARGIN_IN_TIEBREAK);
        boolean isTiebreakWinningSet = highestGames > MINIMUM_GAMES_IN_SET && lowestGames == MINIMUM_GAMES_IN_SET;

        return isDefaultWinningSet || isTiebreakWinningSet;
    }

    protected void finishSet() {
        games.forEach((key, _) -> games.put(key, LOVE));
    }

    protected boolean isReset() {
        return areAllGamesEqualTo(LOVE);
    }
}
