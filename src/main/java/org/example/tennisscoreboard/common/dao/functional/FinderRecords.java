package org.example.tennisscoreboard.common.dao.functional;

import org.example.tennisscoreboard.entity.Match;

import java.util.List;

public interface FinderRecords {

    // Можно назвать AllFinder

    List<Match> find(String indexOfPage); // Из сигнатуры метода не понятно, что за параметр он принимает
}