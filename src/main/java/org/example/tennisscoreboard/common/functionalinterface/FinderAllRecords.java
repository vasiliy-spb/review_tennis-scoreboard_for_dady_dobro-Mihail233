package org.example.tennisscoreboard.common.functionalinterface;

import org.example.tennisscoreboard.entity.Match;

import java.util.List;

public interface FinderAllRecords {
    List<Match> find(String indexOfPage);
}