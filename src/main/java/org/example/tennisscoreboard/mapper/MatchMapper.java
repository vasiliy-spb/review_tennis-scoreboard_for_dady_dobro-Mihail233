package org.example.tennisscoreboard.mapper;

import org.example.tennisscoreboard.dto.MatchResponse;
import org.example.tennisscoreboard.entity.Match;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MatchMapper {

    List<MatchResponse> toDTO(List<Match> match);

    @Mapping(target = "firstPlayerName", source = "playerOne.name")
    @Mapping(target = "secondPlayerName", source = "playerTwo.name")
    @Mapping(target = "winnerName", source = "winner.name")
    default MatchResponse toDTO(Match match) {
        return new MatchResponse(match.getPlayerOne().getName(), match.getPlayerTwo().getName(), match.getWinner().getName());
    }
}
