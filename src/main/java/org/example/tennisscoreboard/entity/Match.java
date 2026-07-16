package org.example.tennisscoreboard.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "Matches")
@Check(name = "one_of_players_is_winner_check", constraints = "(Winner = Player1) OR (Winner = Player2)")
@Check(name = "players_in_match_are_different_check", constraints = "(Player1 != Player2)")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "Player1")
    @NonNull
    private Player playerOne;

    @ManyToOne(optional = false)
    @JoinColumn(name = "Player2")
    @NonNull
    private Player playerTwo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "Winner")
    @NonNull
    private Player winner;
}