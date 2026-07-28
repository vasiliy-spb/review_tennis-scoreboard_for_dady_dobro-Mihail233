package org.example.tennisscoreboard.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;

@Getter
@Setter // TODO: сеттеры не нужны — позволяют создать объект с установленным id или изменить состав игроков после создания (например сделать постороннего игрока победителем)
@NoArgsConstructor // Спецификация JPA требует наличия конструктора без аргументов для создания экземпляров сущностей,
    // однако ему не обязательно быть `public`. Когда конструктор публичный, он становится частью общедоступного API класса.
    // Это позволяет использовать его для создания "пустых", невалидных объектов (без установки обязательных полей)
    // в любом месте приложения, хотя он предназначен исключительно для внутреннего использования фреймворком (JPA).
    //
    // Хорошим подходом будет ограничить область видимости этого конструктора до `protected`.
    // Это делает его недоступным для прямого вызова из других пакетов, но оставляет видимым для JPA и дочерних классов.
    // В Lombok это можно сделать с помощью параметра `access`.
@RequiredArgsConstructor
@Entity
@Table(name = "Matches") // "Matches" является зарезервированным словом в некоторых СУБД.
    // Здесь проблем не будет, но лучше не выбирать такие названия. (см. файл "sql-keywords.md" в этом же пакете)
@Check(name = "one_of_players_is_winner_check", constraints = "(Winner = Player1) OR (Winner = Player2)")
@Check(name = "players_in_match_are_different_check", constraints = "(Player1 != Player2)")
public class Match {

    // Связи `@ManyToOne` не имеют явного указания о стратегии загрузки.
        // По умолчанию для `@ManyToOne` используется `FetchType.EAGER`, что приводит к немедленной загрузке связанных сущностей при загрузке `Match`.
        // Это может вызывать проблемы производительности (N+1 запросов) и излишнюю загрузку данных, особенно если связанные объекты не всегда нужны.

    // Колонки игроков и победителя в `@JoinColumn` названы `Player1`, `Player2`, `Winner`.
        // Для колонок, хранящих внешний ключ, уместно добавлять суффикс `_id`, чтобы было очевидно, что в них хранится идентификатор, а не какая-то другая информация.

    // Здесь игроки называются Player playerOne и Player playerTwo, а в FinishedMatchesPersistenceService — Player firstPlayer и Player secondPlayer.
        // Стоит выбрать один подход для единообразия.
        // А также числительные в названиях колонок БД тоже можно писать буквами для единообразия.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "Player1") // 1. Можно добавить updatable = false. 2. Числительные в названиях колонок тоже можно писать буквами для единообразия.
    @NonNull
    private Player playerOne;

    @ManyToOne(optional = false)
    @JoinColumn(name = "Player2") // 1. Можно добавить updatable = false. 2. Числительные в названиях колонок тоже можно писать буквами для единообразия.
    @NonNull
    private Player playerTwo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "Winner") // Можно добавить updatable = false.
    @NonNull
    private Player winner;
}