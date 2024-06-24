import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, FormsModule, Validators } from '@angular/forms';
import { QuestionSummaryDto } from '../../../dto/QuestionSummaryDto';
import { ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatSelectModule } from '@angular/material/select';
import { MatDividerModule } from '@angular/material/divider';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { QuestionsService } from '../../../services/questions.service';
import { GlobalService } from '../../../services/global.service';

@Component({
  selector: 'app-add-question-summary',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatInputModule,
    MatButtonModule,
    MatCardModule,
    MatFormFieldModule,
    MatIconModule,
    MatSelectModule,
    MatDividerModule,
    FormsModule,
    CommonModule
  ],
  templateUrl: './add-question-summary.component.html',
  styleUrl: './add-question-summary.component.scss'
})
export class AddQuestionSummaryComponent implements OnInit {

  questionSummaryForm!: FormGroup;
  functionalityId?: any;
  constructor(private fb: FormBuilder, private router: ActivatedRoute, private questionService: QuestionsService, private gs: GlobalService) { }

  ngOnInit(): void {
    this.functionalityId = this.router.snapshot.paramMap.get('id');
    this.questionSummaryForm = this.fb.group({
      id: [0, Validators.required],
      summary: ['', Validators.required],
      minimumPreviousQuestionScore: [0, Validators.required],
      questionOrderNumber: [0, Validators.required],
      functionalityId: [0, Validators.required],
      questions: this.fb.array([]),
      scoreSummaries: this.fb.array([])
    });
  }

  get questions(): FormArray {
    return this.questionSummaryForm.get('questions') as FormArray;
  }

  get scoreSummaries(): FormArray {
    return this.questionSummaryForm.get('scoreSummaries') as FormArray;
  }

  addQuestion() {
    const questionFormGroup = this.fb.group({
      id: [0, Validators.required],
      hptuQuestion: ['', Validators.required],
      score: [0, Validators.required],
      questionOrder: [0, Validators.required],
      summaryId: [0, Validators.required]
    });
    this.questions.push(questionFormGroup);
  }

  addScoreSummary() {
    const scoreSummaryFormGroup = this.fb.group({
      id: [0, Validators.required],
      from: [0, Validators.required],
      to: [0, Validators.required],
      summaryColor: ['', Validators.required],
      questionSummaryId: [0, Validators.required]
    });
    this.scoreSummaries.push(scoreSummaryFormGroup);
  }

  onSubmit() {
    if (this.questionSummaryForm.valid) {
      const questionSummaryDto: QuestionSummaryDto = this.questionSummaryForm.value;
      console.log(questionSummaryDto);
      this.questionService.addQuestion(questionSummaryDto, this.functionalityId).subscribe(
        {
          next: (response) => {
            this.gs.openSnackBar("Done sucessfully!!", "Dismiss");
          },
          error: (error) => {
            this.gs.openSnackBar(`An error occured: ${error.error.message}`, "Dismiss");
          }
        });
    }
  }

}
