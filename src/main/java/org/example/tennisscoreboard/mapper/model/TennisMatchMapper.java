package org.example.tennisscoreboard.mapper.model;

import org.example.tennisscoreboard.domain.model.score.TennisMatch;
import org.example.tennisscoreboard.dto.TennisMatchResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TennisMatchMapper {

    @Mapping(target = "firstPlayerName", source = "participants.firstParticipant.name")
    @Mapping(target = "secondPlayerName", source = "participants.secondParticipant.name")

    @Mapping(target = "firstPlayerPoints", source = "generalScore.firstParticipantScore.points.points")
    @Mapping(target = "secondPlayerPoints", source = "generalScore.secondParticipantScore.points.points")

    @Mapping(target = "firstPlayerGames", source = "generalScore.firstParticipantScore.games.games")
    @Mapping(target = "secondPlayerGames", source = "generalScore.secondParticipantScore.games.games")

    @Mapping(target = "firstPlayerSets", source = "generalScore.firstParticipantScore.sets.sets")
    @Mapping(target = "secondPlayerSets", source = "generalScore.secondParticipantScore.sets.sets")

    @Mapping(target = "firstPlayerTieBreakPoints", source = "generalScore.firstParticipantScore.tiebreakPoints.points")
    @Mapping(target = "secondPlayerTieBreakPoints", source = "generalScore.secondParticipantScore.tiebreakPoints.points")

    @Mapping(target = "winnerName", source = "winner.name")
    TennisMatchResponse toDTO(TennisMatch participant);
}
