package org.example.tennisscoreboard.domain.model;

public record Participants(Participant firstParticipant, Participant secondParticipant) {

    // Создавать отдельный класс для пары участников — избыточно. Там где нужно можно просто хранить два поля.

}
