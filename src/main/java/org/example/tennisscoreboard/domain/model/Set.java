package org.example.tennisscoreboard.domain.model;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class Set {

    // Название Set сбивает с толку.
        // А также в java есть интерфейс с названием Set,
        // поэтому создание класса с таким же именем может вводить в заблуждение.
        // Классу, отвечающему за логику обработки счёта в матче, больше подойдёт название TennisMatch.

    private static final int MIN_SETS = 0;
    private static final int MAX_SETS = 2;
    private static final int INITIAL_SETS = 0;

    // TODO: Публичный геттер для этого поля нарушает инкапсуляцию и позволяет любому внешнему коду бесконтрольно изменять внутреннее состояние этого класса.
    // В матче всего два игрока (или две стороны) — создавать Map для сопоставления их со счётом избыточно.
        // Можно просто хранить два поля для игроков и для счёта.
    @Getter
    private Map<String, Integer> sets;

    // Если в классе есть хоть один конструктор, то конструктор по умолчанию
        // (публичный конструктор без аргументов) создан не будет,
        // поэтому не нужно объявлять его как private.
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

    // Удобство использования в тестах не является достаточной причиной для того, чтобы создавать такие методы.
        // Тесты должны писаться для существующего кода и подстраиваться под него, а не наоборот.
        // Этот метод (как и validateSet) стоит удалить.
    protected static Set createCustomSet(String firstParticipantName, int firstParticipantSets, String secondParticipantName,
                                         int secondParticipantSets) {
        validateSet(firstParticipantSets);
        validateSet(secondParticipantSets);
        return new Set(firstParticipantName, firstParticipantSets, secondParticipantName, secondParticipantSets);
    }

    private static void validateSet(int sets) {
        if (sets < MIN_SETS || sets > MAX_SETS) {
            throw new IllegalArgumentException("Invalid sets");
        }
    }

    // Лучше назвать pointWonBy(name)
    protected void updateSets(String name) {
        // TODO: Нет проверки на то, что матч не завершён.
            // Попытка начислить очко в уже завершённом матче — это не нормальная ситуация и
            // должна приводить к исключению.

        // Можно так: sets.merge(name, 1, Integer::sum);
        sets.compute(name, (_, set) -> set + 1);
    }

}
