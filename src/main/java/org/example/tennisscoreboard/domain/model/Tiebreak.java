package org.example.tennisscoreboard.domain.model;

import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.example.tennisscoreboard.domain.model.Game.MINIMUM_POINT_MARGIN_IN_TIEBREAK;

public class Tiebreak {

    // Слово Tiebreak можно убрать из названий методов и констант — этот контекст понятен из названия самого класса.

    private static final int MIN_POINTS = 0;
    private static final int INITIAL_TIEBREAK_POINTS = 0;

    // Для этой константы тоже стоит использовать примитивный тип int
    // Лучше назвать MINIMUM_POINTS_TO_WIN
    private static final Integer MINIMUM_POINTS_IN_TIEBREAK = 7;

    // TODO: Публичный геттер для этого поля нарушает инкапсуляцию и позволяет любому внешнему коду бесконтрольно изменять внутреннее состояние этого класса.
    // В тай-брейке всего два игрока (или две стороны) — создавать Map для сопоставления их со счётом избыточно.
        // Можно просто хранить два поля для игроков и для счёта.
    @Getter
    private Map<String, Integer> tiebreakPoints;

    // Если в классе есть хоть один конструктор, то конструктор по умолчанию
        // (публичный конструктор без аргументов) создан не будет,
        // поэтому не нужно объявлять его как private.
    private Tiebreak() {}

    private Tiebreak(String firstParticipantName, Integer firstParticipantPoints, String secondParticipantName, Integer secondParticipantPoints) {
        initTiebreakPoints(firstParticipantName, firstParticipantPoints, secondParticipantName, secondParticipantPoints);
    }

    private void initTiebreakPoints(String firstParticipantName, Integer firstParticipantPoints, String secondParticipantName, Integer secondParticipantPoints) {
        tiebreakPoints = new HashMap<>();
        tiebreakPoints.put(firstParticipantName, firstParticipantPoints);
        tiebreakPoints.put(secondParticipantName, secondParticipantPoints);
    }

    protected static Tiebreak createTiebreak(String firstParticipantName, String secondParticipantName) {
        return new Tiebreak(firstParticipantName, null, secondParticipantName, null);
    }

    // Удобство использования в тестах не является достаточной причиной для того, чтобы создавать такие методы.
        // Тесты должны писаться для существующего кода и подстраиваться под него, а не наоборот.
        // Этот метод (как и validateTiebreak) стоит удалить.
    protected static Tiebreak createCustomTiebreak(String firstParticipantName, Integer firstParticipantPoints, String secondParticipantName, Integer secondParticipantPoints) {
        validateTiebreak(firstParticipantPoints);
        validateTiebreak(secondParticipantPoints);
        return new Tiebreak(firstParticipantName, firstParticipantPoints, secondParticipantName, secondParticipantPoints);
    }

    private static void validateTiebreak(int points) {
        if (points < MIN_POINTS) {
            throw new IllegalArgumentException("Invalid tiebreak points");
        }
    }

    protected void updatePointInTiebreak(String name) {
        // TODO: Нет проверки на то, что тай-брейк не завершён.
            // Попытка начислить очко в уже завершённом тай-брейке — это не нормальная ситуация и
            // должна приводить к исключению.

        if (isReset()) {
            startTiebreak();
        }

        incrementPointInTiebreak(name);

        // Этот if читается как "если тай-брейк закончен, то закончить тай-брейк"
        if (isTiebreakFinished()) {
            finishTiebreak();
        }
    }

    // В теннисе нет понятия "сбросить тай-брейк (или счёт)", поэтому классу, представляющему тай-брейк,
        // достаточно иметь метод boolean isFinished().
    protected boolean isReset() {
        return tiebreakPoints.values().stream()
                .allMatch(points -> Objects.equals(points, null));
    }

    // Лучше назвать initScore
    private void startTiebreak() {
        tiebreakPoints.forEach((key, _) -> tiebreakPoints.put(key, INITIAL_TIEBREAK_POINTS));
    }

    // Можно назвать addPointTo(name)
    private void incrementPointInTiebreak(String name) {
        // Можно так: tiebreakPoints.merge(name, 1, Integer::sum);
        tiebreakPoints.compute(name, (_, tiebreakPoint) -> tiebreakPoint + 1);
    }

    // Использование методов Collections.max()/Collections.min() порождает создание лишних объектов (итераторов).
        // Переход на хранение счёта в двух переменных исправит это.
    private boolean isTiebreakFinished() {
        int highestPoints = Collections.max(tiebreakPoints.values());
        int lowestPoints = Collections.min(tiebreakPoints.values());

        // TODO: Константа MINIMUM_POINT_MARGIN_IN_TIEBREAK должна находиться в этом классе.
        return highestPoints >= MINIMUM_POINTS_IN_TIEBREAK && (highestPoints - lowestPoints >= MINIMUM_POINT_MARGIN_IN_TIEBREAK);
    }

    // Реальному теннисному матчу больше бы соответствовал подход, где сет содержит несколько тай-брейков/геймов,
        // а не обнуляет счёт одного и того же объекта.
    private void finishTiebreak() {
        // В текущей реализации можно просто вызвать tiebreakPoints.clear();
        tiebreakPoints.forEach((key, _) -> tiebreakPoints.put(key, null));
    }
}
