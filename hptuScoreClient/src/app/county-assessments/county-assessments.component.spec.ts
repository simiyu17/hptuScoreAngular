import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CountyAssessmentsComponent } from './county-assessments.component';

describe('CountyAssessmentsComponent', () => {
  let component: CountyAssessmentsComponent;
  let fixture: ComponentFixture<CountyAssessmentsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CountyAssessmentsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CountyAssessmentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
