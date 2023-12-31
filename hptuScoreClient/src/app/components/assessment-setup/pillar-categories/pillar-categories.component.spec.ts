import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PillarCategoriesComponent } from './pillar-categories.component';

describe('PillarCategoriesComponent', () => {
  let component: PillarCategoriesComponent;
  let fixture: ComponentFixture<PillarCategoriesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PillarCategoriesComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PillarCategoriesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
