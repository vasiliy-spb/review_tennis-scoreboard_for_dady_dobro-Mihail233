package org.example.tennisscoreboard.mapper;

import org.example.tennisscoreboard.dto.MatchResponse;
import org.example.tennisscoreboard.entity.Match;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring") // Для "spring" в mapstruct есть специальная константа: MappingConstants.ComponentModel.SPRING
public interface MatchMapper {

    // Можно назвать EntityDtoMatchMapper

    // Точнее назвать toDtoList
    List<MatchResponse> toDTO(List<Match> match);

    // Писать и аннотации и реализацию метода избыточно.
        // Можно оставить только аннотации @Mapping и объявление метода:
        // MatchResponse toDTO(Match match)
        // Его реализация будет сгенерирована автоматически
    @Mapping(target = "firstPlayerName", source = "playerOne.name")
    @Mapping(target = "secondPlayerName", source = "playerTwo.name")
    @Mapping(target = "winnerName", source = "winner.name")
    default MatchResponse toDTO(Match match) {
        return new MatchResponse(match.getPlayerOne().getName(), match.getPlayerTwo().getName(), match.getWinner().getName());
    }
}
