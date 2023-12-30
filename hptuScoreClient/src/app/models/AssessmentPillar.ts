import { MatTableDataSource } from "@angular/material/table";
import { PillarCategory } from "./PillarCategory";
import { CountyAssessment } from "./CountyAssessment";
import { FormArray } from "@angular/forms";

export interface AssessmentPillar {
    id: number;
    pillarName: string;
    pillarOrder: number;
    pillarCategoryCount: number;
    categories?: PillarCategory[];
    dataSource: CountyAssessment[];
    formArray: FormArray;
}