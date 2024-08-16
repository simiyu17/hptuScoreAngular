import { FormArray } from "@angular/forms";
import { QuestionDto } from "./QuestionDto";
import { ScoreSummarySetUpDto } from "./ScoreSummarySetUpDto";
import { HptuAssessmentDto } from "./HptuAssessmentDto";

export interface QuestionSummaryDto {
    id: number;
    summary: string;
    minimumPreviousQuestionScore: number;
    questionOrderNumber: number;
    functionalityId: number;
    questions: QuestionDto[]
    scoreSummaries: ScoreSummarySetUpDto[]
    dataSource: HptuAssessmentDto[]
    formArray: FormArray;
}