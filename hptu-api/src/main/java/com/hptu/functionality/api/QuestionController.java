package com.hptu.functionality.api;


import com.hptu.functionality.dto.QuestionSummaryDto;
import com.hptu.functionality.service.QuestionService;
import com.hptu.shared.dto.ApiResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/hptu-questions")
@Validated
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping("{functionality-id}")
    public ResponseEntity<ApiResponseDto> createQuestions(@PathVariable("functionality-id") Long functionalityId,  @Valid @RequestBody QuestionSummaryDto questionSummaryDto) {
        try {
            this.questionService.createQuestions(functionalityId, questionSummaryDto);
            return new ResponseEntity<>(new ApiResponseDto(true, "Question was submitted!!"), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponseDto(true, "An Error Occurred: "+e.getMessage()), HttpStatus.BAD_REQUEST);
        }

    }
}
