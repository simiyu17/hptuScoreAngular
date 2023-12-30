import { CountyAssessment } from "../models/CountyAssessment";
import { CountyAssessmentStatus } from "../models/CountyAssessmentStatus";

export interface CountyAssessmentDto {
    assessmentStatus: CountyAssessmentStatus;
    assessments: CountyAssessment[];
}