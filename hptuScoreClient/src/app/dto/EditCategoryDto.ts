import { NewPillarCategory } from "../models/NewPillarCategory";
import { PillarCategory } from "../models/PillarCategory";

export interface EditCategoryDto {
    category: PillarCategory | NewPillarCategory;
    pillarId: number
}