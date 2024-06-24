package com.hptu.functionality.mapper;

import com.hptu.functionality.domain.QuestionSummary;
import com.hptu.functionality.dto.QuestionSummaryDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ScoreSummarySetUpMapper.class, QuestionMapper.class})
public interface QuestionSummaryMapper {

    @Mapping(target = "functionalityId", expression = "java(questionSummary.getFunctionality().getId())")
    QuestionSummaryDto toDto(QuestionSummary questionSummary);

    List<QuestionSummaryDto> toDto(List<QuestionSummary> questionSummary);
}
