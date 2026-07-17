package org.example.tennisscoreboard.domain.model;

import lombok.Getter;

public class Participant {
    private static int MIN_LENGTH_NAME = 5;
    private static int MAX_LENGTH_NAME = 20;
    private static int MIN_ID = 1;

    @Getter
    private Long id;

    @Getter
    private String name;

    private Participant() {}

    private Participant(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    //для валидации
    public static Participant createParticipant(Long id, String name) {
        validateParticipant(id, name);
        return new Participant(id, name);
    }

    private static void validateParticipant(Long id, String name) {
        if (name.isBlank() || name.length() < MIN_LENGTH_NAME || name.length() > MAX_LENGTH_NAME) {
            throw new IllegalArgumentException("Invalid participant name");
        }

        if (id < MIN_ID) {
            throw new IllegalArgumentException("Invalid participant id");
        }
    }
}
