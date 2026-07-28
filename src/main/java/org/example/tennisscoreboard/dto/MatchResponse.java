package org.example.tennisscoreboard.dto;

public record MatchResponse(String firstPlayerName, String secondPlayerName, String winnerName) {

    // Можно назвать FinishedMatchResponse

}
