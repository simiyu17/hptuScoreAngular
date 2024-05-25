export interface CountyAssessmentMetaData {
    id?: number
    assessmentQuarter: string | null;
    assessmentYear: string | null;
    assessmentLevel: string | null;
    countyCode: string | null;
    countyName?: string;
}