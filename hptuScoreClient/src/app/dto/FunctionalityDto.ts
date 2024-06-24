import { QuestionSummaryDto } from "./QuestionSummaryDto";

export interface FunctionalityDto {
    id: number;
    hptuName: string;
    questions: QuestionSummaryDto[]
    description: string;
    functionalityOrder: number;
}