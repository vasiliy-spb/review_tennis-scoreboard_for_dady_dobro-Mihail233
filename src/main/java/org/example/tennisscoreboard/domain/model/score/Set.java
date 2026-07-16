package org.example.tennisscoreboard.domain.model.score;

import lombok.Getter;

public class Set {
    private static int INITIAL_SETS = 0;

    @Getter
    private int sets;

    private Set() {};

    private Set(int set) {
        this.sets = set;
    }

    protected void incrementSets() {
        sets++;
    }

    protected static Set createSet() {
        return new Set(INITIAL_SETS);
    }

    protected static Set createSpecificSet(int sets) {
        return new Set(sets);
    }
}
