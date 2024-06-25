package com.hptu.functionality.mapper;

import com.hptu.functionality.domain.ScoreSummarySetUp;
import com.hptu.functionality.dto.ScoreSummarySetUpDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValueMappingStrategy;

@Mapper(componentModel = "spring", nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface ScoreSummarySetUpMapper {

    @Mapping(target = "questionSummaryId", expression = "java(scoreSummarySetUp.getQuestionSummary().getId())")
    @Mapping(target = "from", expression = "java(scoreSummarySetUp.getFrom())")
    ScoreSummarySetUpDto toDto(ScoreSummarySetUp scoreSummarySetUp);

    List<ScoreSummarySetUpDto> toDto(List<ScoreSummarySetUp> scoreSummarySetUps);
}
