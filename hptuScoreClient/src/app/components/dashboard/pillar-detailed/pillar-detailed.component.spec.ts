import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PillarDetailedComponent } from './pillar-detailed.component';

describe('PillarDetailedComponent', () => {
  let component: PillarDetailedComponent;
  let fixture: ComponentFixture<PillarDetailedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PillarDetailedComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PillarDetailedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
