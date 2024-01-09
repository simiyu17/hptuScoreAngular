import { CountyAssessment } from "../models/CountyAssessment";
import { CountyAssessmentMetaData } from "../models/CountyAssessmentMetaData";

export interface CountyAssessmentDto {
    assessmentMetaDataDto: CountyAssessmentMetaData;
    assessments: CountyAssessment[];
}