package org.example.tennisscoreboard.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
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

    //@OneToMany - один игрок множество матчей, при этом возратятся все матчи игрока
}