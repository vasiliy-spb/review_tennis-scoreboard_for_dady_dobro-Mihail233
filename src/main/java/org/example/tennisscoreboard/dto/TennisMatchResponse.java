package org.example.tennisscoreboard.dto;

public record TennisMatchResponse (

        // Можно назвать OngoingMatchResponse

        // Сейчас все поля, относящиеся к счёту игрока, дублируются для первого и второго игрока.
            // Такой подход делает классы большими и громоздкими и нарушает принцип DRY (Don't Repeat Yourself).
            // Также, чтобы добавить счёт в тай-брейке для каждого игрока, понадобится добавить два поля.
            // Можно ввести DTO для счёта одного игрока и хранить два таких DTO внутри MatchScoreDto.
            // Вероятно сейчас причина этому — ошибка в ТЗ

        // Для полей, которые не могут быть null можно использовать примитивные типы

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
