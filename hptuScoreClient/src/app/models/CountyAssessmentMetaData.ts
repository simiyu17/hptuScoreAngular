export interface CountyAssessmentMetaData {
    id?: number
    assessmentQuarter: string | null | undefined;
    assessmentYear: string | null | undefined;
    assessmentLevel: string | null | undefined;
    countyCode: string | null | undefined;
    countyName?: string;
}