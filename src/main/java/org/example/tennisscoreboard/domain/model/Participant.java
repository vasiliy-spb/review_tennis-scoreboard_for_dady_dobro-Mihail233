package org.example.tennisscoreboard.domain.model;

import lombok.Getter;

public class Participant {
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
        //добавить валидацию
        return new Participant(id, name);
    }
}
