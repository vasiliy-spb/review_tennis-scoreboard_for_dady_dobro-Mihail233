package org.example.tennisscoreboard.domain.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public class TennisMatchTest {
    private static final String FIRST_PARTICIPANT_NAME = "badBoy52";
    private static final String SECOND_PARTICIPANT_NAME = "goodGirl67";

    public static final int AD = -1;
    public static final int LOVE = 0;

    private static final Participants PARTICIPANTS = new Participants(
            Participant.createParticipant(1L, FIRST_PARTICIPANT_NAME),
            Participant.createParticipant(2L, SECOND_PARTICIPANT_NAME)
    );

    @Nested
    class MatchScoreCalculationServicePointsTest {

        @ParameterizedTest
        @MethodSource("org.example.tennisscoreboard.domain.model.TennisMatchTest#getArgumentsForFinishOneGameTest")
        void finishOneGameTest(String participantName, TennisMatch tennisMatch) {
            tennisMatch.awardPoint(participantName);

            Assertions.assertTrue(
                    areAllEqualTo(tennisMatch.getScore().getPoint().getPoints(), LOVE) &&
                            areAllEqualTo(tennisMatch.getScore().getTiebreakPoint().getTiebreakPoints(), null)
            );
        }

        @ParameterizedTest
        @MethodSource("org.example.tennisscoreboard.domain.model.TennisMatchTest#getArgumentsForContinueOneGameTest")
        void continueOneGameTest(String participantName, TennisMatch tennisMatch) {
            tennisMatch.awardPoint(participantName);

            Assertions.assertFalse(
                    areAllEqualTo(tennisMatch.getScore().getPoint().getPoints(), LOVE) &&
                            areAllEqualTo(tennisMatch.getScore().getTiebreakPoint().getTiebreakPoints(), null)
            );
        }

        @Test
        void updateScoreFailWhenInvalidScore() {
            Assertions.assertThrows(IllegalArgumentException.class, () -> TennisMatch.createCustomMatchWithoutTiebreak(PARTICIPANTS,
                    31, 31, 4,
                    6, 6,0 ));
        }
    }

    @Nested
    class MatchScoreCalculationServiceGamesTest {

        @ParameterizedTest
        @MethodSource("org.example.tennisscoreboard.domain.model.TennisMatchTest#getArgumentsForFinishOneSetTest")
        void finishOneSetTest(String participantName, TennisMatch tennisMatch) {
            tennisMatch.awardPoint(participantName);
            Assertions.assertTrue(
                    areAllEqualTo(tennisMatch.getScore().getPoint().getPoints(), LOVE) &&
                            areAllEqualTo(tennisMatch.getScore().getTiebreakPoint().getTiebreakPoints(), null)
                    && areAllEqualTo(tennisMatch.getScore().getGame().getGames(), 0)
            );
        }

        @ParameterizedTest
        @MethodSource("org.example.tennisscoreboard.domain.model.TennisMatchTest#getArgumentsForContinueOneSetTest")
        void continueOneSetTest(String participantName, TennisMatch tennisMatch) {
            tennisMatch.awardPoint(participantName);
            Assertions.assertFalse(
                    areAllEqualTo(tennisMatch.getScore().getPoint().getPoints(), LOVE) &&
                            areAllEqualTo(tennisMatch.getScore().getTiebreakPoint().getTiebreakPoints(), null)
                            && areAllEqualTo(tennisMatch.getScore().getGame().getGames(), 0)
            );
        }
    }

    @Nested
    class MatchCalculationServiceSetsTest {

        @ParameterizedTest
        @MethodSource("org.example.tennisscoreboard.domain.model.TennisMatchTest#getArgumentsForFinishedOneMatchTest")
        void finishOneMatchTest(String participantName, TennisMatch tennisMatch) {
            tennisMatch.awardPoint(participantName);
            Assertions.assertNotNull(tennisMatch.getWinner());
        }

        @ParameterizedTest
        @MethodSource("org.example.tennisscoreboard.domain.model.TennisMatchTest#getArgumentsForContinueOneMatchTest")
        void continueOneMatch(String participantName, TennisMatch tennisMatch) {
            tennisMatch.awardPoint(participantName);
            Assertions.assertNull(tennisMatch.getWinner());
        }
    }

    static Stream<Arguments> getArgumentsForFinishOneGameTest() {
        return Stream.of(
                Arguments.of(
                        FIRST_PARTICIPANT_NAME,
                        TennisMatch.createCustomMatchWithoutTiebreak(PARTICIPANTS,
                                40, 0, 0, 0,
                                0, 0)
                ),
                Arguments.of(
                        FIRST_PARTICIPANT_NAME,
                        TennisMatch.createCustomMatchWithoutTiebreak(PARTICIPANTS,
                                AD, 40, 0, 0,
                                0, 0)
                ),
                //tiebreak
                Arguments.of(
                        FIRST_PARTICIPANT_NAME,
                        TennisMatch.createCustomMatchWithTiebreak(PARTICIPANTS,
                                0, 0, 6, 6,
                                0, 0, 6, 0
                        )
                )
        );
    }

    static Stream<Arguments> getArgumentsForContinueOneGameTest() {
        return Stream.of(
                Arguments.of(
                        FIRST_PARTICIPANT_NAME,
                        TennisMatch.createCustomMatchWithoutTiebreak(PARTICIPANTS,
                                30, 40, 0, 0,
                                0, 0)
                ),
                Arguments.of(
                        FIRST_PARTICIPANT_NAME,
                        TennisMatch.createCustomMatchWithoutTiebreak(PARTICIPANTS,
                                40, 40, 0, 0,
                                0, 0)
                ),
                Arguments.of(
                        SECOND_PARTICIPANT_NAME,
                        TennisMatch.createCustomMatchWithoutTiebreak(PARTICIPANTS,
                                AD, 40, 0, 0,
                                0, 0
                        )
                ),
                //tiebreak
                Arguments.of(
                        FIRST_PARTICIPANT_NAME,
                        TennisMatch.createCustomMatchWithTiebreak(PARTICIPANTS,
                                0, 0, 6, 6,
                                0, 0, 6, 7
                        )
                ),
                Arguments.of(
                        SECOND_PARTICIPANT_NAME,
                        TennisMatch.createCustomMatchWithTiebreak(PARTICIPANTS,
                                0, 0, 6, 6,
                                0, 0, 67, 66
                        )
                )
        );
    }

    static Stream<Arguments> getArgumentsForFinishOneSetTest() {
        return Stream.of(
                Arguments.of(
                        FIRST_PARTICIPANT_NAME,
                        TennisMatch.createCustomMatchWithoutTiebreak(PARTICIPANTS,
                                40, 0, 5, 0,
                                0, 0)
                ),
                Arguments.of(
                        FIRST_PARTICIPANT_NAME,
                        TennisMatch.createCustomMatchWithoutTiebreak(PARTICIPANTS,
                                40, 0, 6, 5,
                                0, 0)
                ),
                //tiebreak
                Arguments.of(
                        FIRST_PARTICIPANT_NAME,
                        TennisMatch.createCustomMatchWithTiebreak(PARTICIPANTS,
                                0, 0, 6, 6,
                                0, 0,6,5)
                )

        );
    }

    static Stream<Arguments> getArgumentsForContinueOneSetTest() {
        return Stream.of(
                Arguments.of(
                        FIRST_PARTICIPANT_NAME,
                        TennisMatch.createCustomMatchWithoutTiebreak(PARTICIPANTS,
                                40, 0, 5, 6,
                                0,0)
                ),
                Arguments.of(
                        FIRST_PARTICIPANT_NAME,
                        TennisMatch.createCustomMatchWithTiebreak(PARTICIPANTS,
                                0, 0, 6, 6,
                                0,0,0,0)
                )
        );
    }

    static Stream<Arguments> getArgumentsForFinishedOneMatchTest() {
        return Stream.of(
                Arguments.of(
                        FIRST_PARTICIPANT_NAME,
                        TennisMatch.createCustomMatchWithoutTiebreak(PARTICIPANTS,
                                40, 0, 6, 5,
                                1,0)
                ),
                Arguments.of(
                        FIRST_PARTICIPANT_NAME,
                        TennisMatch.createCustomMatchWithoutTiebreak(PARTICIPANTS,
                                40, 0, 5, 4,
                                1,1)
                )
        );
    }

    static Stream<Arguments> getArgumentsForContinueOneMatchTest() {
        return Stream.of(
                Arguments.of(
                        SECOND_PARTICIPANT_NAME,
                        TennisMatch.createCustomMatchWithoutTiebreak(PARTICIPANTS,
                                0, 40, 0, 5,
                                0,0)
                ),
                Arguments.of(
                        SECOND_PARTICIPANT_NAME,
                        TennisMatch.createCustomMatchWithoutTiebreak(PARTICIPANTS,
                                0, 40, 0, 5,
                                1,0)
                )
        );
    }

    private boolean areAllEqualTo(Map<String, Integer> map, Integer assertion) {
        return map.values().stream()
                .allMatch(points -> Objects.equals(points, assertion));
    }
}
