//package org.example.tennisscoreboard.mapper.entity;
//
//import org.example.tennisscoreboard.dto.MatchDTO;
//import org.example.tennisscoreboard.entity.hibernate.Match;
//import org.example.tennisscoreboard.mapper.PlayerMapper;
//import org.mapstruct.Mapper;
//import org.mapstruct.NullValuePropertyMappingStrategy;
//import org.mapstruct.factory.Mappers;
//
//import java.util.List;
//
//@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = PlayerMapper.class)
//public interface MatchMapper {
//    MatchMapper INSTANCE = Mappers.getMapper(MatchMapper.class);
//
//    MatchDTO toDTO(Match match);
//
//    Match toObject(MatchDTO matchDTO);
//
//    List<MatchDTO> toDTO(List<Match> matches);
//}
