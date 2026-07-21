package org.example.tennisscoreboard.domain.model;

import lombok.Getter;

import java.util.Objects;
import java.util.UUID;

public class TennisMatch {
    private static final int SETS_TO_WIN_MATCH = 2;

    @Getter
    private UUID uuid;

    @Getter
    private Participants participants;

    @Getter
    private Score score;

    @Getter
    private Participant winner;

    private TennisMatch() {
    }

    private TennisMatch(Participants participants, Score score) {
//        this.uuid = UUID.randomUUID();
        this.uuid = UUID.fromString("f609a413-255a-4eae-925a-dedddd67e470");
        this.participants = participants;
        this.score = score;
    }

    public static TennisMatch createMatch(Participants participants) {
        String firstParticipantName = participants.firstParticipant().getName();
        String secondParticipantName = participants.secondParticipant().getName();

        Score score = Score.createScore(firstParticipantName,secondParticipantName);
        return new TennisMatch(participants, score);
    }

    public void awardPoint(String winnerName) {
        score.awardPointByName(winnerName);
        findWinner();
    }

    private void findWinner() {
        Set set = score.getSet();

        set.getSets().forEach((key, value) -> {
            if (value.equals(SETS_TO_WIN_MATCH)) {
                winner = (Objects.equals(participants.firstParticipant().getName(), key)) ? participants.firstParticipant() : participants.secondParticipant();
            }
        });
    }
}
