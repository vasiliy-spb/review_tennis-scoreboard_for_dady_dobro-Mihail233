package org.example.tennisscoreboard.common.dao;

public interface ExtendedDAO<K, E> extends DAO<K, E> {
    int countAllMatches();
    int countAllMatchesByPlayerName(String playerName);
    E findMatchesByNameAndPage(String playerName, String pageNumber);
}
