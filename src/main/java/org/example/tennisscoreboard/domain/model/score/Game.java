package org.example.tennisscoreboard.domain.model.score;

import lombok.Getter;

public class Game {
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

    protected void setGames(int games) {
        this.games = games;
    }

    protected static Game createGame() {
        return new Game(INITIAL_GAMES);
    }

    protected static Game createSpecificGame(int games) {
        return new Game(games);
    }
}
