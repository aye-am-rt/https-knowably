import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ExpertAnalyticsGraphComponent } from './expert-analytics-graph.component';

describe('ExpertAnalyticsGraphComponent', () => {
  let component: ExpertAnalyticsGraphComponent;
  let fixture: ComponentFixture<ExpertAnalyticsGraphComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ExpertAnalyticsGraphComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ExpertAnalyticsGraphComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
