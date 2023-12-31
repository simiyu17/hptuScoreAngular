import { CountyAssessment } from "../models/CountyAssessment";
import { CountyAssessmentMetaData } from "../models/CountyAssessmentStatus";

export interface CountyAssessmentDto {
    assessmentMetaDataDto: CountyAssessmentMetaData;
    assessments: CountyAssessment[];
}