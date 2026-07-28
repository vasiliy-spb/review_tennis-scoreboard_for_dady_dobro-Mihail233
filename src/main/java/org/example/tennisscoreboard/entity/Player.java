package org.example.tennisscoreboard.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter // TODO: сеттеры не нужны — позволяют создать объект с установленным id или изменить имя игрока после создания
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
@Table(name = "Players", indexes = {
        @Index(columnList = "name", unique = true, name = "player_name_index")
})
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Имя игрока не может быть пустым")
    @Size(min = 5, max = 20, message = "Имя должно быть от 5 до 20 символов")
    @NonNull
    @Column(unique = true)
    private String name;

    // Стоит удалять комментарии (вроде того, что указан в следующей строке) из кода перед тем, как выполнять коммит
    //@OneToMany - один игрок множество матчей, при этом возратятся все матчи игрока
}