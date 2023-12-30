export interface CountyAssessment {
    id?: number;
    pillarId?: number;
    category?: string;
    choiceOne?: string;
    choiceOneScore?: number;
    choiceTwo?: string;
    choiceTwoScore?: number;
    choiceThree?: string;
    choiceThreeScore?: number;
    choiceFour?: string;
    choiceFourScore?: number;
    categoryOrder?: number;
    choiceText?: string;
    choiceScore?: number;
    maxScore?: number;
    scoreRemarks: string;
}