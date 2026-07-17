package org.example.tennisscoreboard.domain.model.score;

import lombok.Getter;

public class Set {
    private static int MIN_SETS = 0;
    private static int MAX_SETS = 2;
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
        validateSet(sets);
        return new Set(sets);
    }

    private static void validateSet(int sets) {
        if (sets < MIN_SETS || sets > MAX_SETS) {
            throw new IllegalArgumentException("Invalid sets");
        }
    }
}
