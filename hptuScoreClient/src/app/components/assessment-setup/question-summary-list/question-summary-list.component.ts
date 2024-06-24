import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormArray, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatListModule } from '@angular/material/list';
import { MatExpansionModule } from '@angular/material/expansion';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-question-summary-list',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatInputModule,
    MatButtonModule,
    MatCardModule,
    MatFormFieldModule,
    MatSelectModule,
    MatIconModule,
    MatListModule,
    MatTableModule,
    MatExpansionModule,
    CommonModule
  ],
  templateUrl: './question-summary-list.component.html',
  styleUrl: './question-summary-list.component.scss'
})
export class QuestionSummaryListComponent implements OnInit {
  questionSummaryForm!: FormGroup;
  dataSource!: MatTableDataSource<any>;
  displayedColumns: string[] = ['id', 'summary', 'minimumPreviousQuestionScore', 'questionOrderNumber', 'functionalityId'];
  expandedElement: any | null;

  constructor(private fb: FormBuilder) { }

  ngOnInit(): void {
    this.questionSummaryForm = this.fb.group({
      id: [''],
      summary: [''],
      minimumPreviousQuestionScore: [''],
      questionOrderNumber: [''],
      functionalityId: [''],
      questionsd: this.fb.array([]),
      scoreSummaries: this.fb.array([])
    });

    this.dataSource = new MatTableDataSource(this.getQuestionSummaryData());
  }

  get questionsd(): FormArray {
    return this.questionSummaryForm.get('questionsd') as FormArray;
  }

  get scoreSummaries(): FormArray {
    return this.questionSummaryForm.get('scoreSummaries') as FormArray;
  }

  addQuestion(): void {
    this.questionsd.push(this.fb.group({
      id: [''],
      hptuQuestion: [''],
      score: [''],
      questionOrder: [''],
      summaryId: ['']
    }));
  }

  addScoreSummary(): void {
    this.scoreSummaries.push(this.fb.group({
      id: [''],
      fromd: [''],
      tod: [''],
      summaryColor: [''],
      questionSummaryId: ['']
    }));
  }

  onSubmit(): void {
    console.log(this.questionSummaryForm.value);
  }

  getQuestionSummaryData() {
    // Mock data for demonstration
    return [
      {
        id: 1,
        summary: 'Summary 1',
        minimumPreviousQuestionScore: 10,
        questionOrderNumber: 1,
        functionalityId: 1,
        questionsd: [
          { id: 1, hptuQuestion: 'Question 1', score: 5, questionOrder: 1, summaryId: 1 },
          { id: 2, hptuQuestion: 'Question 2', score: 10, questionOrder: 2, summaryId: 1 }
        ],
        scoreSummaries: [
          { id: 1, fromd: 0, tod: 5, summaryColor: 'red', questionSummaryId: 1 },
          { id: 2, fromd: 6, tod: 10, summaryColor: 'green', questionSummaryId: 1 }
        ]
      }
    ];
  }

}
