package org.example.tennisscoreboard.domain.model;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class Set {
    private static final int INITIAL_SETS = 0;

    @Getter
    private Map<String, Integer> sets;

    private Set() {
    }

    private Set(String firstParticipantName, int firstParticipantSets, String secondParticipantName, int secondParticipantSets) {
        initSets(firstParticipantName, firstParticipantSets, secondParticipantName, secondParticipantSets);
    }

    private void initSets(String firstParticipantName, int firstParticipantSets, String secondParticipantName, int secondParticipantSets) {
        sets = new HashMap<>();
        sets.put(firstParticipantName, firstParticipantSets);
        sets.put(secondParticipantName, secondParticipantSets);
    }

    protected static Set createSet(String firstParticipantName, String secondParticipantName) {
        return new Set(firstParticipantName, INITIAL_SETS, secondParticipantName, INITIAL_SETS);
    }

    protected void updateSets(String name) {
        sets.compute(name, (_, set) -> set + 1);
    }

}
