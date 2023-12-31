import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AssessmentGraphComponent } from './assessment-graph.component';

describe('AssessmentGraphComponent', () => {
  let component: AssessmentGraphComponent;
  let fixture: ComponentFixture<AssessmentGraphComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AssessmentGraphComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AssessmentGraphComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
