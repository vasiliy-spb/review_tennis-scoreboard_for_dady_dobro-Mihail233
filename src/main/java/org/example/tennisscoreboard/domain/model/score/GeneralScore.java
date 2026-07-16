package org.example.tennisscoreboard.domain.model.score;

import lombok.Getter;

import static org.example.tennisscoreboard.domain.model.score.Score.*;

public class GeneralScore {
    //название поля поменять под функцию проверки что сейчас тайбрейк
    private static final int MINIMUM_POINTS_IN_TIEBREAK = 7;
    private static final int MINIMUM_GAMES_IN_SET = 6;
    private static final int MINIMUM_POINT_MARGIN_IN_TIEBREAK = 2;

    @Getter
    private Score firstParticipantScore;

    @Getter
    private Score secondParticipantScore;

    private GeneralScore() {
    }

    protected GeneralScore(Score firstParticipantScore, Score secondParticipantScore) {
        this.firstParticipantScore = firstParticipantScore;
        this.secondParticipantScore = secondParticipantScore;
    }

    protected static GeneralScore createDefaultGeneralScore() {
        return new GeneralScore(Score.createDefaultScore(), Score.createDefaultScore());
    }

    protected static GeneralScore createSpecificGeneralScore(int firstPlayerPoints, int secondPlayerPoints, int firstPlayerGames, int secondPlayerGames,
                                                             int firstPlayerSets, int secondPlayerSets, int firstPlayerTiebreakPoints, int secondPlayerTiebreakPoints) {
        return new GeneralScore(Score.createSpecificScore(firstPlayerPoints, firstPlayerGames, firstPlayerSets, firstPlayerTiebreakPoints),
                Score.createSpecificScore(secondPlayerPoints, secondPlayerGames, secondPlayerSets, secondPlayerTiebreakPoints));
    }

    protected void awardPointFirstParticipant() {
        awardPointCertainParticipant(firstParticipantScore, secondParticipantScore);
    }

    protected void awardPointSecondParticipant() {
        awardPointCertainParticipant(secondParticipantScore, firstParticipantScore);
    }

    private void awardPointCertainParticipant(Score winnerScore, Score loserScore) {
        updatePoint(winnerScore, loserScore);
        updateGame(winnerScore, loserScore);
        updateSet(winnerScore, loserScore);
    }

    private void updatePoint(Score winnerScore, Score loserScore) {
        if (isTiebreak(winnerScore, loserScore)) {
            updatePointInTiebreak(winnerScore, loserScore);
        } else {
            updatePointInStandardGame(winnerScore, loserScore);
        }
    }

    private boolean isTiebreak(Score winnerScore, Score loserScore) {
        return winnerScore.getGames().getGames() == MINIMUM_GAMES_IN_SET && loserScore.getGames().getGames() == MINIMUM_GAMES_IN_SET;
    }

    private void updatePointInTiebreak(Score winnerScore, Score loserScore) {
        if (isTiebreakRequired(winnerScore, loserScore)) {
            startTiebreak(winnerScore, loserScore);
        }

        winnerScore.incrementPointInTiebreak();

        if (isTiebreakFinished(winnerScore, loserScore)) {
            finishTiebreak(winnerScore, loserScore);
        }
    }

    private boolean isTiebreakRequired(Score winnerScore, Score loserScore) {
        return winnerScore.getTiebreakPoints() == null && loserScore.getTiebreakPoints() == null;
    }

    private void startTiebreak(Score winnerScore, Score loserScore) {
        winnerScore.createTiebreak();
        loserScore.createTiebreak();
    }

    private boolean isTiebreakFinished(Score winnerScore, Score loserScore) {
        int highestPoints = Math.max(winnerScore.getTiebreakPoints().getPoints(), loserScore.getTiebreakPoints().getPoints());
        int lowestPoints = Math.min(winnerScore.getTiebreakPoints().getPoints(), loserScore.getTiebreakPoints().getPoints());

        return highestPoints >= MINIMUM_POINTS_IN_TIEBREAK && (highestPoints - lowestPoints >= MINIMUM_POINT_MARGIN_IN_TIEBREAK);
    }

    private void finishTiebreak(Score winnerScore, Score loserScore) {
        winnerScore.deleteTiebreak();
        loserScore.deleteTiebreak();
    }

    private void updatePointInStandardGame(Score winnerScore, Score loserScore) {

        if (isDeuce(winnerScore, loserScore)) {
            startAdvantageGame(winnerScore);
        } else if (isAdvantage(winnerScore, loserScore)) {
            returnToDeuceOrFinishAdvantageGame(winnerScore, loserScore);
        } else {
            continueOrFinishStandardGame(winnerScore, loserScore);
        }
    }

    private boolean isDeuce(Score winnerScore, Score loserScore) {
        return winnerScore.getPoints().getPoints() == THREE_POINT && loserScore.getPoints().getPoints() == THREE_POINT;
    }

    private void startAdvantageGame(Score winnerScore) {
        winnerScore.createAdvantageGame();
    }

    private boolean isAdvantage(Score winnerScore, Score loserScore) {
        return winnerScore.getPoints().getPoints() == AD || loserScore.getPoints().getPoints() == AD;
    }

    private void returnToDeuceOrFinishAdvantageGame(Score winnerScore, Score loserScore) {
        if (winnerScore.getPoints().getPoints() == AD) {
            finishGame(winnerScore, loserScore);
        } else {
            loserScore.returnToThreePoint();
        }
    }

    private void finishGame(Score winnerScore, Score loserScore) {
        winnerScore.resetStandardPoints();
        loserScore.resetStandardPoints();
    }

    private void continueOrFinishStandardGame(Score winnerScore, Score loserScore) {
        Point winnerPoints = winnerScore.getPoints();
        if (winnerPoints.getPoints() == THREE_POINT) {
            finishGame(winnerScore, loserScore);
        } else {
            winnerScore.awardPoints();
        }
    }

    private void updateGame(Score winnerScore, Score loserScore) {
        if (isEndedGame(winnerScore, loserScore)) {
            winnerScore.incrementGames();
        }

        if (isWinningSet(winnerScore, loserScore)) {
            finishSet(winnerScore, loserScore);
        }
    }

    private void finishSet(Score winnerScore, Score loserScore) {
        winnerScore.resetGames();
        loserScore.resetGames();
    }

    private boolean isEndedGame(Score winnerScore, Score loserScore) {
        return winnerScore.getPoints().getPoints() == LOVE &&
                loserScore.getPoints().getPoints() == LOVE &&
                winnerScore.getTiebreakPoints() == null &&
                loserScore.getTiebreakPoints() == null;
    }

    private boolean isWinningSet(Score winnerScore, Score loserScore) {

        int highestGames = Math.max(winnerScore.getGames().getGames(), loserScore.getGames().getGames());
        int lowestGames = Math.min(winnerScore.getGames().getGames(), loserScore.getGames().getGames());

        boolean isDefaultWinningSet = highestGames >= MINIMUM_GAMES_IN_SET && (highestGames - lowestGames >= MINIMUM_POINT_MARGIN_IN_TIEBREAK);
        boolean isTiebreakWinningSet = highestGames > MINIMUM_GAMES_IN_SET && lowestGames == MINIMUM_GAMES_IN_SET;

        return isDefaultWinningSet || isTiebreakWinningSet;
    }

    private void updateSet(Score winnerScore, Score loserScore) {
        if (isEndedSet(winnerScore, loserScore)) {
            winnerScore.incrementSets();
        }
    }

    private boolean isEndedSet(Score winnerScore, Score loserScore) {
        return winnerScore.getPoints().getPoints() == LOVE &&
                loserScore.getPoints().getPoints() == LOVE &&
                winnerScore.getGames().getGames() == LOVE &&
                loserScore.getGames().getGames() == LOVE;
    }
}
