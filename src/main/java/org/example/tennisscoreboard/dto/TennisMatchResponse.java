package org.example.tennisscoreboard.dto;

public record TennisMatchResponse (
        String firstPlayerName,
        String secondPlayerName,
        String firstPlayerPoints,
        String secondPlayerPoints,
        Integer firstPlayerGames,
        Integer secondPlayerGames,
        Integer firstPlayerSets,
        Integer secondPlayerSets,
        Integer firstPlayerTieBreakPoints,
        Integer secondPlayerTieBreakPoints,
        String winnerName
) {
}
