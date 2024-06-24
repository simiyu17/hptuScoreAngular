package com.hptu.functionality.mapper;

import com.hptu.functionality.domain.Question;
import com.hptu.functionality.dto.QuestionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface QuestionMapper {

    @Mapping(target = "summaryId", expression = "java(question.getSummary().getId())")
    @Mapping(target = "hptuQuestion", expression = "java(question.getHptuQuestion())")
    QuestionDto toDto(Question question);

    List<QuestionDto> toDto(List<Question> questions);
}
