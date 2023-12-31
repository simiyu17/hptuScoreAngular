import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PillarSummaryComponent } from './pillar-summary.component';

describe('PillarSummaryComponent', () => {
  let component: PillarSummaryComponent;
  let fixture: ComponentFixture<PillarSummaryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PillarSummaryComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PillarSummaryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
