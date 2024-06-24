package com.hptu.functionality.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.hptu.functionality.dto.QuestionSummaryDto;
import com.hptu.shared.domain.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
@Table(name = "question_summary", uniqueConstraints = { @UniqueConstraint(columnNames = { "question_order_num" }, name = "HPTU_Q_UNIQUE_ORDER")})
public class QuestionSummary extends BaseEntity {

    @Column(length = 2048)
    private String summary;

    private int minimumPreviousQuestionScore;

    @Column(name = "question_order_num")
    private int questionOrderNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference("functionality")
    private Functionality functionality;

    @Setter
    @OneToMany(mappedBy = "summary", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("summary")
    private Set<Question> questions = new HashSet<>();

    @Setter
    @OneToMany(mappedBy = "questionSummary", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("questionSummary")
    private Set<ScoreSummarySetUp> scoreSummaries = new HashSet<>();

    public QuestionSummary() {
    }

    private QuestionSummary(String summary, int minimumPreviousQuestionScore, int questionOrderNumber, Functionality functionality) {
        this.summary = summary;
        this.minimumPreviousQuestionScore = minimumPreviousQuestionScore;
        this.questionOrderNumber = questionOrderNumber;
        this.functionality = functionality;
    }

    public static QuestionSummary createQuestionSummary(QuestionSummaryDto questionSummaryDto, Functionality functionality){
        return new QuestionSummary(questionSummaryDto.summary(), questionSummaryDto.minimumPreviousQuestionScore(), questionSummaryDto.questionOrderNumber(), functionality);
    }

    public void updateQuestionSummary(QuestionSummaryDto questionSummaryDto){
        if(!StringUtils.equals(questionSummaryDto.summary(), this.summary)){
            this.summary = questionSummaryDto.summary();
        }
        if(questionSummaryDto.minimumPreviousQuestionScore() != this.minimumPreviousQuestionScore){
            this.minimumPreviousQuestionScore = questionSummaryDto.minimumPreviousQuestionScore();
        }
        if(questionSummaryDto.questionOrderNumber() != this.questionOrderNumber){
            this.questionOrderNumber = questionSummaryDto.questionOrderNumber();
        }
    }

    public void addQuestions(Set<Question> questions){
        this.getQuestions().addAll(questions);
    }

    public void addScoreSummary(Set<ScoreSummarySetUp> summaries){
        this.getScoreSummaries().addAll(summaries);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        QuestionSummary questionSummary = (QuestionSummary) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o)).append(getId(), questionSummary.getId())
                .append(getSummary(), questionSummary.getSummary())
                .append(getQuestionOrderNumber(), questionSummary.getQuestionOrderNumber())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(getId()).append(getSummary())
                .append(getQuestionOrderNumber()).toHashCode();
    }
}
