package org.example.tennisscoreboard.entity;

import jakarta.persistence.*;
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

    @NonNull
    @Column(unique = true, length = 20)
    private String name;

    //@OneToMany - один игрок множество матчей, при этом возратятся все матчи игрока
}