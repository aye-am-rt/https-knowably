import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ExpertAnalyticsMedicalComponent } from './expert-analytics-medical.component';

describe('ExpertAnalyticsMedicalComponent', () => {
  let component: ExpertAnalyticsMedicalComponent;
  let fixture: ComponentFixture<ExpertAnalyticsMedicalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ExpertAnalyticsMedicalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ExpertAnalyticsMedicalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
