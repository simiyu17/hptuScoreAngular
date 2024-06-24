package com.hptu.functionality.mapper;

import com.hptu.functionality.domain.Functionality;
import com.hptu.functionality.dto.FunctionalityDto;
import org.mapstruct.Mapper;

import java.util.List;
import org.mapstruct.NullValueMappingStrategy;

@Mapper(componentModel = "spring", uses = QuestionSummaryMapper.class, nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface FunctionalityMapper {

    FunctionalityDto toDto(Functionality functionality);

    List<FunctionalityDto> toDto(List<Functionality> functionalities);
}
