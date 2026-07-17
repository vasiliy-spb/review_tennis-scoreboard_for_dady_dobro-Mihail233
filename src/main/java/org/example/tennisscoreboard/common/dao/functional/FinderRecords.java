package org.example.tennisscoreboard.common.dao.functional;

import org.example.tennisscoreboard.entity.Match;

import java.util.List;

public interface FinderRecords {
    List<Match> find(String indexOfPage);
}