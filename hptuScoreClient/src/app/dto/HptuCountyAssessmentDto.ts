import { HptuAssessmentDto } from "./HptuAssessmentDto";

export interface HptuCountyAssessmentDto {
    countyCode: string | null;
    assessmentDate: string | null;
    assessments: HptuAssessmentDto[];
}