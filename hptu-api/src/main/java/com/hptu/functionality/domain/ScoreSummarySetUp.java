package com.hptu.functionality.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.hptu.functionality.dto.QuestionDto;
import com.hptu.functionality.dto.ScoreSummarySetUpDto;
import com.hptu.shared.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Entity
@Table(name = "score_summary_set_up", uniqueConstraints = { @UniqueConstraint(columnNames = { "score_from", "score_to", "question_summary_id" }, name = "HPTU_Q_UNIQUE_ORDER")})
public class ScoreSummarySetUp extends BaseEntity {

    @Column(name = "score_from")
    private Integer from;

    @Column(name = "score_to")
    private Integer to;

    private String summaryColor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference("questionSummary")
    @JoinColumn(name = "question_summary_id")
    private QuestionSummary questionSummary;

    public ScoreSummarySetUp() {
    }

    private ScoreSummarySetUp(Integer from, Integer to, String summaryColor, QuestionSummary questionSummary) {
        this.from = from;
        this.to = to;
        this.summaryColor = summaryColor;
        this.questionSummary = questionSummary;
    }

    public static ScoreSummarySetUp createScoreSummarySetUp(final ScoreSummarySetUpDto scoreSummarySetUpDto, final QuestionSummary summary){
        return new ScoreSummarySetUp(scoreSummarySetUpDto.from(), scoreSummarySetUpDto.to(), scoreSummarySetUpDto.summaryColor(), summary);
    }

    public static Set<ScoreSummarySetUp> createScoreSummarySetUps(final List<ScoreSummarySetUpDto> summarySetUpDtos, final QuestionSummary summary){
        return summarySetUpDtos.stream().map(scoreSummarySetUpDto -> new ScoreSummarySetUp(scoreSummarySetUpDto.from(), scoreSummarySetUpDto.to(), scoreSummarySetUpDto.summaryColor(), summary)).collect(Collectors.toSet());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ScoreSummarySetUp scoreSummary = (ScoreSummarySetUp) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o)).append(getId(), scoreSummary.getId())
                .append(getFrom(), scoreSummary.getFrom())
                .append(getTo(), scoreSummary.getTo())
                .append(getQuestionSummary(), scoreSummary.getQuestionSummary()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(getId()).append(getFrom())
                .append(getTo())
                .append(getQuestionSummary()).toHashCode();
    }
}
