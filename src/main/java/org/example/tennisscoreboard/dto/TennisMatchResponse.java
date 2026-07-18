package org.example.tennisscoreboard.dto;

public record TennisMatchResponse (
        String firstPlayerName,
        String secondPlayerName,
        String firstPlayerPoints,
        String secondPlayerPoints,
        String firstPlayerGames,
        String secondPlayerGames,
        String firstPlayerSets,
        String secondPlayerSets,
        String firstPlayerTieBreakPoints,
        String secondPlayerTieBreakPoints,
        String winnerName
) {
}
