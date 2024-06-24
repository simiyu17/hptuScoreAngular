package com.hptu.functionality.mapper;

import com.hptu.functionality.domain.Functionality;
import com.hptu.functionality.dto.FunctionalityDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = QuestionSummaryMapper.class)
public interface FunctionalityMapper {

    FunctionalityDto toDto(Functionality functionality);

    List<FunctionalityDto> toDto(List<Functionality> functionalities);
}
