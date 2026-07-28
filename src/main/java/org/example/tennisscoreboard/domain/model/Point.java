package org.example.tennisscoreboard.domain.model;

import lombok.Getter;

import java.util.*;

public class Point {

    // TODO: Класс нарушает Принцип единой ответственности (SRP). Он:
        // - отвечает за преставление счёта в гейме
        // - отвечает за логику обработки счёта в гейме

    // Название Point сбивает с толку. Point — подходящее название для класса,
        // представляющего модель счёта в обычном гейме.
        // А классу, отвечающему за логику обработки счёта в гейме, больше подойдёт название RegularGame.

    // TODO: "Кодирование" счёта.
        // Класс, представляющий модель счёта в обычном гейме может (и должен) быть перечислением (enum).
        // Так как он имеет ограниченное число уникальных значений.

    // В теннисном гейме нет понятия одно очко, два очка (ONE_POINT, TWO_POINT) и тд.
        // Вместо этого счёт произносится как "пятнадцать", "тридцать" и тд.
        // Поэтому константам стоит дать именно такие названия.

    private static final int INITIAL_POINTS = 0;

    // Все константы для счёта должны быть private
    public static final int AD = -1; // У состояния "преимущество" нет числового значения. Оно обозначается как AD или ADVANTAGE.
    public static final int LOVE = 0;
    public static final int ONE_POINT = 15;
    public static final int TWO_POINT = 30;
    public static final int THREE_POINT = 40;

    // Чтобы список значений был неизменяемым его не нужно оборачивать в new ArrayList<>()
    private static final ArrayList<Integer> ENABLE_POINT = new ArrayList<>(List.of(AD, LOVE, ONE_POINT, TWO_POINT, THREE_POINT));

    // TODO: Публичный геттер для этого поля нарушает инкапсуляцию и позволяет любому внешнему коду бесконтрольно изменять внутреннее состояние этого класса.
    // В гейме всего два игрока (или две стороны) — создавать Map для сопоставления их со счётом избыточно.
        // Можно просто хранить два поля для игроков и для счёта.
    @Getter
    private Map<String, Integer> points;

    // Если в классе есть хоть один конструктор, то конструктор по умолчанию
        // (публичный конструктор без аргументов) создан не будет,
        // поэтому не нужно объявлять его как private.
    private Point() {
    }

    private Point(String firstParticipantName, int firstParticipantPoints, String secondParticipantName, int secondParticipantPoints) {
        initPoints(firstParticipantName, firstParticipantPoints, secondParticipantName, secondParticipantPoints);
    }

    private void initPoints(String firstParticipantName, int firstParticipantPoints, String secondParticipantName, int secondParticipantPoints) {
        points = new HashMap<>();
        points.put(firstParticipantName, firstParticipantPoints);
        points.put(secondParticipantName, secondParticipantPoints);
    }

    protected static Point createPoint(String firstParticipantName, String secondParticipantName) {
        return new Point(firstParticipantName, INITIAL_POINTS, secondParticipantName, INITIAL_POINTS);
    }

    // Удобство использования в тестах не является достаточной причиной для того, чтобы создавать такие методы.
        // Тесты должны писаться для существующего кода и подстраиваться под него, а не наоборот.
        // Этот метод (как и validatePoints) стоит удалить.
    protected static Point createCustomPoint(String firstParticipantName, int firstParticipantPoints, String secondParticipantName,
                                             int secondParticipantPoints) {

        validatePoints(firstParticipantPoints);
        validatePoints(secondParticipantPoints);
        return new Point(firstParticipantName, firstParticipantPoints, secondParticipantName, secondParticipantPoints);
    }

    private static void validatePoints(int points) {

        if (!ENABLE_POINT.contains(points)) {
            throw new IllegalArgumentException("Invalid points");
        }
    }

    // Метод, запускающий обработку выигранного очка должен быть публичным.
    // Поскольку в проекте есть доменная модель участника матча (Participant),
        // в этот метод лучше принимать её, а не отдельно имя игрока.
        // Или можно ввести enum TennisSide {FIRST, SECOND} и сделать аргумент этого типа.
    protected void updatePointInStandardGame(String name) {
        // TODO: Нет проверки на то, что гейм не завершён.
            // Попытка начислить очко в уже завершённом гейме — это не нормальная ситуация и
            // должна приводить к исключению.

        if (isDeuce()) {
            startAdvantageGame(name);
        } else if (isAdvantage()) {
            returnToDeuceOrFinishAdvantageGame(name);
        } else {
            continueOrFinishStandardGame(name);
        }
    }

    private boolean isDeuce() {
        return areAllPointsEqualTo(THREE_POINT);
    }

    private boolean areAllPointsEqualTo(int point) {
        return points.values().stream()
                .allMatch(points -> Objects.equals(points, point));
    }

    // Лучше назвать setAdvantageTo(name)
    private void startAdvantageGame(String name) {
        points.put(name, AD);
    }

    // Название метода стоит уточнить. Сейчас не понятно у кого он проверяет наличие преимущества.
    private boolean isAdvantage() {
        return points.values().stream()
                .anyMatch(points -> Objects.equals(points, AD));
    }

    // Слово return в java имеет определённое значение, поэтому лучше его не использовать в названиях методов.
    // Можно назвать handleAdvantage
    private void returnToDeuceOrFinishAdvantageGame(String name) {
        Integer winnerPoints = points.get(name);

        if (winnerPoints == AD) {
            finishGame();
        } else {
            returnToDeuce(name);
        }
    }

    // Реальному теннисному матчу больше бы соответствовал подход, где сет содержит несколько геймов,
        // а не обнуляет счёт одного и того же объекта.
    private void finishGame() {
        points.forEach((key, _) -> points.put(key, LOVE));
    }

    // Слово return в java имеет определённое значение, поэтому лучше его не использовать в названиях методов
    private void returnToDeuce(String name) {
        points.forEach((key, _) -> {
            if (!Objects.equals(key, name)) {
                points.put(key, THREE_POINT);
            }
        });
    }

    // Метод нарушает Принцип единой ответственности на уровне метода —
        // выполняет более одной логично связанной операции.
        // Стоит разделить его на два метода или провести другой рефакторинг, который исправит это.
    private void continueOrFinishStandardGame(String name) {
        Integer winnerPoints = points.get(name);

        if (winnerPoints == THREE_POINT) {
            finishGame();
        } else {
            awardPoints(name);
        }
    }

    private void awardPoints(String name) {
        Integer winnerPoints = points.get(name);
        switch (winnerPoints) {
            case (LOVE):
                points.put(name, ONE_POINT);
                break;
            case (ONE_POINT):
                points.put(name, TWO_POINT);
                break;
            case (TWO_POINT):
                points.put(name, THREE_POINT);
                break;
            default:
                throw new IllegalArgumentException("Unexpected value");
        }
    }

    // В теннисе нет понятия "сбросить гейм (или счёт)", поэтому классу, представляющему гейм,
        // лучше иметь метод boolean isFinished().
    protected boolean isReset() {
        return areAllPointsEqualTo(LOVE);
    }
}
