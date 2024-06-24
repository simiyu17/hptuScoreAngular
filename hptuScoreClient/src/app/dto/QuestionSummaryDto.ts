import { QuestionDto } from "./QuestionDto";
import { ScoreSummarySetUpDto } from "./ScoreSummarySetUpDto";

export interface QuestionSummaryDto {
    id: number;
    summary: string;
    minimumPreviousQuestionScore: number;
    questionOrderNumber: number;
    functionalityId: number;
    questions: QuestionDto[]
    scoreSummaries: ScoreSummarySetUpDto[]
}