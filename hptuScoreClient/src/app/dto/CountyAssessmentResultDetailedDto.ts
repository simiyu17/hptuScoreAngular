import { CountySummaryDto } from "./CountySummaryDto";

export interface CountyAssessmentResultDetailedDto {
    summary: CountySummaryDto[];
    summaryDataPoints: {x: string, y: number}[]
}