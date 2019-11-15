import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ExpertAnalyticsComponent } from './expert-analytics.component';

describe('ExpertAnalyticsComponent', () => {
  let component: ExpertAnalyticsComponent;
  let fixture: ComponentFixture<ExpertAnalyticsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ExpertAnalyticsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ExpertAnalyticsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
