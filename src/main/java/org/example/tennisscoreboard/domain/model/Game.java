package org.example.tennisscoreboard.domain.model;

import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.example.tennisscoreboard.domain.model.Point.LOVE;

public class Game {

    // Название Game сбивает с толку.
        // Классу, отвечающему за логику обработки счёта в сете, больше подойдёт название TennisSet.

    private static final int MIN_GAMES = 0;
    private static final int MAX_GAMES = 7;

    // Для этой константы тоже стоит использовать примитивный тип int
    // Модификатор этой константы должен быть private
    public static final Integer MINIMUM_GAMES_IN_SET = 6;

    // Для этой константы тоже стоит использовать примитивный тип int
    // TODO: Константа MINIMUM_POINT_MARGIN_IN_TIEBREAK должна находиться в классе тай-брейка.
    public static final Integer MINIMUM_POINT_MARGIN_IN_TIEBREAK = 2;

    private static final int INITIAL_GAMES = 0;

    // TODO: Публичный геттер для этого поля нарушает инкапсуляцию и позволяет любому внешнему коду бесконтрольно изменять внутреннее состояние этого класса.
    // В сете всего два игрока (или две стороны) — создавать Map для сопоставления их со счётом избыточно.
        // Можно просто хранить два поля для игроков и для счёта.
    @Getter
    private Map<String, Integer> games;

    // Если в классе есть хоть один конструктор, то конструктор по умолчанию
        // (публичный конструктор без аргументов) создан не будет,
        // поэтому не нужно объявлять его как private.
    private Game() {
    }

    private Game(String firstParticipantName, int firstParticipantGames, String secondParticipantName, int secondParticipantGames) {
        initGames(firstParticipantName, firstParticipantGames, secondParticipantName, secondParticipantGames);
    }

    private void initGames(String firstParticipantName, int firstParticipantGames, String secondParticipantName, int secondParticipantGames) {
        games = new HashMap<>();
        games.put(firstParticipantName, firstParticipantGames);
        games.put(secondParticipantName, secondParticipantGames);
    }

    protected static Game createGame(String firstParticipantName, String secondParticipantName) {
        return new Game(firstParticipantName, INITIAL_GAMES, secondParticipantName, INITIAL_GAMES);
    }

    // Удобство использования в тестах не является достаточной причиной для того, чтобы создавать такие методы.
        // Тесты должны писаться для существующего кода и подстраиваться под него, а не наоборот.
        // Этот метод (как и validateGames) стоит удалить.
    protected static Game createCustomGame(String firstParticipantName, int firstParticipantGames, String secondParticipantName,
                                           int secondParticipantGames) {
        validateGames(firstParticipantGames);
        validateGames(secondParticipantGames);
        return new Game(firstParticipantName, firstParticipantGames, secondParticipantName, secondParticipantGames);
    }

    private static void validateGames(int games) {
        if (games > MAX_GAMES || games < MIN_GAMES) {
            throw new IllegalArgumentException("Invalid games");
        }
    }

    // Метод с названием "является ли тай-брейком" в классе, отвечающем за логику в сете, сбивает с толку.
        // Название метода должно означать "нужно ли начать тай-брейк" и метод должен быть приватным.
        // За создание тай-брейка должен отвечать этот класс.
    protected boolean isTiebreak() {
        return areAllGamesEqualTo(MINIMUM_GAMES_IN_SET);
    }

    private boolean areAllGamesEqualTo(int game) {
        return games.values().stream()
                .allMatch(points -> Objects.equals(points, game));
    }

    // Лучше назвать pointWonBy(name)
    protected void updateGames(String name) {
        // TODO: Нет проверки на то, что сет не завершён.
            // Попытка начислить очко в уже завершённом сете — это не нормальная ситуация и
            // должна приводить к исключению.

        incrementGames(name);

        if (isWinningSet()) {
            finishSet();
        }
    }

    // TODO: Метод нарушает инкапсуляцию и позволяет классам в этом же пакете изменять своё состояние в любое время.
        // Этот метод должен быть private
    protected void incrementGames(String name) {
        // Можно так: games.merge(name, 1, Integer::sum);
        games.compute(name, (_, game) -> game + 1);
    }

    // Отсутствует явный модификатор доступа
    // Использование методов Collections.max()/Collections.min() порождает создание лишних объектов (итераторов).
        // Переход на хранение счёта в двух переменных исправит это.
    // Название метода читается как "является ли выигрышным сетом". Лучше назвать isFinished().
    boolean isWinningSet() {

        int highestGames = Collections.max(games.values());
        int lowestGames = Collections.min(games.values());

        boolean isDefaultWinningSet = highestGames >= MINIMUM_GAMES_IN_SET && (highestGames - lowestGames >= MINIMUM_POINT_MARGIN_IN_TIEBREAK);
        boolean isTiebreakWinningSet = highestGames > MINIMUM_GAMES_IN_SET && lowestGames == MINIMUM_GAMES_IN_SET;

        return isDefaultWinningSet || isTiebreakWinningSet;
    }

    // TODO: Метод нарушает инкапсуляцию и позволяет классам в этом же пакете изменять своё состояние в любое время.
        // Этот метод должен быть private
    // TODO: Значение Point.LOVE не относится к счёту в сете. Использовать здесь эту константу неуместно.
    protected void finishSet() {
        games.forEach((key, _) -> games.put(key, LOVE));
    }

    // TODO: Значение Point.LOVE не относится к счёту в сете. Использовать здесь эту константу неуместно.
    // В теннисе нет понятия "сбросить сет (или счёт)", поэтому классу, представляющему сет,
        // достаточно иметь метод boolean isFinished().
    protected boolean isReset() {
        return areAllGamesEqualTo(LOVE);
    }
}
