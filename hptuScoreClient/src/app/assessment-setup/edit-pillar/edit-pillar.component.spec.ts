import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditPillarComponent } from './edit-pillar.component';

describe('EditPillarComponent', () => {
  let component: EditPillarComponent;
  let fixture: ComponentFixture<EditPillarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditPillarComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(EditPillarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
