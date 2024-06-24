package com.hptu.functionality.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.hptu.functionality.dto.QuestionDto;
import com.hptu.shared.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Entity
@Table(name = "questions")
public class Question extends BaseEntity {

    @Column(length = 2048)
    private String hptuQuestion;

    private int score;

    private int questionOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference("summary")
    private QuestionSummary summary;

    public Question() {
    }

    private Question(String question, int score, int questionOrder, QuestionSummary summary) {
        this.hptuQuestion = question;
        this.score = score;
        this.summary = summary;
        this.questionOrder = questionOrder;
    }

    public static Question createQuestion(final QuestionDto questionDto, final QuestionSummary summary){
        return new Question(questionDto.hptuQuestion(), questionDto.score(), questionDto.questionOrder(), summary);
    }

    public static Set<Question> createQuestions(final List<QuestionDto> questionDtos, final QuestionSummary summary){
        return questionDtos.stream().map(q -> new Question(q.hptuQuestion(), q.score(),q.questionOrder(), summary)).collect(Collectors.toSet());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Question question1 = (Question) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o)).append(getId(), question1.getId())
                .append(getHptuQuestion(), question1.getHptuQuestion())
                .append(getScore(), question1.getScore())
                .append(getSummary(), question1.getSummary()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(getId()).append(getHptuQuestion())
                .append(getScore())
                .append(getSummary()).toHashCode();
    }
}
