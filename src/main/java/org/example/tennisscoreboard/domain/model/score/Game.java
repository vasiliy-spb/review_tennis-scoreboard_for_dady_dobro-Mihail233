package org.example.tennisscoreboard.domain.model.score;

import jakarta.validation.constraints.Min;
import lombok.Getter;

public class Game {
    private static int MIN_GAMES = 0;
    private static int MAX_GAMES = 7;

    private static int INITIAL_GAMES = 0;

    @Getter
    private int games;

    private Game() {};

    private Game(int games) {
        this.games = games;
    }

    protected void incrementGames() {
        games++;
    }

    protected void resetGames() {
        this.games = MIN_GAMES;
    }

    protected static Game createGame() {
        return new Game(INITIAL_GAMES);
    }

    protected static Game createSpecificGame(int games) {
        validateGames(games);
        return new Game(games);
    }

    protected static void validateGames(int games) {
        if (games > MAX_GAMES || games < MIN_GAMES) {
            throw new IllegalArgumentException("Invalid games");
        }
    }
}
