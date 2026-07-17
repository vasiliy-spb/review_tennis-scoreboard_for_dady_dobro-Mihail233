package org.example.tennisscoreboard.domain.model.score;

import lombok.Getter;
import org.example.tennisscoreboard.domain.model.Participant;
import org.example.tennisscoreboard.domain.model.Participants;

import java.util.UUID;

public class TennisMatch {
    private static final int SETS_TO_WIN_MATCH = 2;

    @Getter
    private UUID uuid;

    @Getter
    private Participants participants;

    @Getter
    private GeneralScore generalScore;

    @Getter
    private Participant winner;

    private TennisMatch() {
    }

    //идея aggregator root
    private TennisMatch(Participants participants, GeneralScore generalScore) {
//        this.uuid = UUID.randomUUID();
        this.uuid = UUID.fromString("f609a413-255a-4eae-925a-dedddd67e470");
        this.participants = participants;
        this.generalScore = generalScore;
    }

    public static TennisMatch createMatch(Participants participants) {
        GeneralScore generalScore = GeneralScore.createDefaultGeneralScore();
        return new TennisMatch(participants, generalScore);
    }

    public static TennisMatch createMatchWithSpecificScore(Participants participants, int firstPlayerPoints, int secondPlayerPoint, int firstPlayerGames, int secondPlayerGames,
                                                           int firstPlayerSets, int secondPlayerSets, int firstPlayerTiebreakPoints, int secondPlayerTiebreakPoints) {
        GeneralScore generalScore = GeneralScore.createSpecificGeneralScore(firstPlayerPoints, secondPlayerPoint, firstPlayerGames, secondPlayerGames,
                firstPlayerSets, secondPlayerSets, firstPlayerTiebreakPoints, secondPlayerTiebreakPoints);
        return new TennisMatch(participants, generalScore);
    }

    //можно добавить метод задать начальное состояние

    public void awardPoint(String winnerName) {
        awardPointCertainParticipant(winnerName);
        findWinner();
    }

    private void awardPointCertainParticipant(String winnerName) {
        String firstParticipantName = participants.firstParticipant().getName();

        if (firstParticipantName.equals(winnerName)) {
            generalScore.awardPointFirstParticipant();
        } else {
            //даже если имя неправильное награжет 2
            generalScore.awardPointSecondParticipant();
        }
    }

    private void findWinner() {
        if (isFirstParticipantWinner()) {
            winner = participants.firstParticipant();
        } else if (isSecondParticipantWinner()) {
            winner = participants.secondParticipant();
        }
    }

    private boolean isFirstParticipantWinner() {
        return generalScore.getFirstParticipantScore().getSets().getSets() == SETS_TO_WIN_MATCH;
    }

    private boolean isSecondParticipantWinner() {
        return generalScore.getSecondParticipantScore().getSets().getSets() == SETS_TO_WIN_MATCH;
    }
}
