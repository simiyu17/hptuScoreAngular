import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreatePillarComponent } from './create-pillar.component';

describe('CreatePillarComponent', () => {
  let component: CreatePillarComponent;
  let fixture: ComponentFixture<CreatePillarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreatePillarComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CreatePillarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
