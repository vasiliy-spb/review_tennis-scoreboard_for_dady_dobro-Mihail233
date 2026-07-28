package org.example.tennisscoreboard.common.dao;

public interface ExtendedDAO<K, E> extends DAO<K, E> {

    // Интерфейс параметризованный, но методы имеют названия, специфичные для DAO матчей.
        // Дженерики не нужны этому интерфейсу.

    // Использование `long` для счётчиков, получаемых из БД, является более правильной практикой,
        // так как SQL-функция `COUNT` возвращает 64-битное число.
    int countAllMatches();

    // Использование `long` для счётчиков, получаемых из БД, является более правильной практикой,
        // так как SQL-функция `COUNT` возвращает 64-битное число.
    int countAllMatchesByPlayerName(String playerName);

    // TODO: Реализация этого метода в PostgresFinishedMatchDAO возвращает на E, а List<E>
    // TODO: Название метода говорит, что он "ищет матчи по имени [игрока] и номеру страницы",
        // хотя в реализации String pageNumber превращается в String offset.
        // Корректная сигнатура метода выборки матчей может быть такой:
        // List<Match> findAllByPlayerName(String playerName, int offset, int limit)
    E findMatchesByNameAndPage(String playerName, String pageNumber); // TODO: Числа должны передаваться числами (int)
}
