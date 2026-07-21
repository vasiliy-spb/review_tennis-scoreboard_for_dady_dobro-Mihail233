package org.example.tennisscoreboard.mapper;

import org.example.tennisscoreboard.domain.model.*;
import org.example.tennisscoreboard.dto.TennisMatchResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TennisMatchMapper {

    @Mapping(target = "firstPlayerName", expression = "java(mapFirstPlayerName(tennisMatch))")
    @Mapping(target = "secondPlayerName", expression = "java(mapSecondPlayerName(tennisMatch))")

    @Mapping(target = "firstPlayerPoints", expression = "java(mapFirstPlayerPoints(tennisMatch))")
    @Mapping(target = "secondPlayerPoints", expression = "java(mapSecondPlayerPoints(tennisMatch))")

    @Mapping(target = "firstPlayerGames", expression = "java(mapFirstPlayerGames(tennisMatch))")
    @Mapping(target = "secondPlayerGames", expression = "java(mapSecondPlayerGames(tennisMatch))")

    @Mapping(target = "firstPlayerSets", expression = "java(mapFirstPlayerSets(tennisMatch))")
    @Mapping(target = "secondPlayerSets", expression = "java(mapSecondPlayerSets(tennisMatch))")

    @Mapping(target = "firstPlayerTieBreakPoints", expression = "java(mapFirstPlayerTiebreak(tennisMatch))")
    @Mapping(target = "secondPlayerTieBreakPoints", expression = "java(mapSecondPlayerTiebreak(tennisMatch))")

    @Mapping(target = "winnerName", source = "winner.name")
    TennisMatchResponse toDTO(TennisMatch tennisMatch);

    default String mapFirstPlayerName(TennisMatch tennisMatch) {
        return tennisMatch.getParticipants().firstParticipant().getName();
    }

    default String mapSecondPlayerName(TennisMatch tennisMatch) {
        return tennisMatch.getParticipants().secondParticipant().getName();
    }

    default String mapFirstPlayerPoints(TennisMatch tennisMatch) {
        Point point = tennisMatch.getScore().getPoint();
        Integer firstPlayerPoints = point.getPoints().get(tennisMatch.getParticipants().firstParticipant().getName());
        return (firstPlayerPoints == -1) ? "AD": String.valueOf(firstPlayerPoints);
    }

    default String mapSecondPlayerPoints(TennisMatch tennisMatch) {
        Point point = tennisMatch.getScore().getPoint();
        Integer secondPlayerPoints = point.getPoints().get(tennisMatch.getParticipants().secondParticipant().getName());
        return (secondPlayerPoints == -1) ? "AD": String.valueOf(secondPlayerPoints);
    }

    default Integer mapFirstPlayerGames(TennisMatch tennisMatch) {
        Game game = tennisMatch.getScore().getGame();
        return game.getGames().get(tennisMatch.getParticipants().firstParticipant().getName());
    }

    default Integer mapSecondPlayerGames(TennisMatch tennisMatch) {
        Game game = tennisMatch.getScore().getGame();
        return game.getGames().get(tennisMatch.getParticipants().secondParticipant().getName());
    }

    default Integer mapFirstPlayerSets(TennisMatch tennisMatch) {
        Set set = tennisMatch.getScore().getSet();
        return set.getSets().get(tennisMatch.getParticipants().firstParticipant().getName());
    }
    default Integer mapSecondPlayerSets(TennisMatch tennisMatch) {
        Set set = tennisMatch.getScore().getSet();
        return set.getSets().get(tennisMatch.getParticipants().secondParticipant().getName());
    }

    default Integer mapFirstPlayerTiebreak(TennisMatch tennisMatch) {
        Tiebreak tiebreak = tennisMatch.getScore().getTiebreakPoint();
        return tiebreak.getTiebreakPoints().get(tennisMatch.getParticipants().firstParticipant().getName());
    }

    default Integer mapSecondPlayerTiebreak(TennisMatch tennisMatch) {
        Tiebreak tiebreak = tennisMatch.getScore().getTiebreakPoint();
        return tiebreak.getTiebreakPoints().get(tennisMatch.getParticipants().secondParticipant().getName());
    }
}
