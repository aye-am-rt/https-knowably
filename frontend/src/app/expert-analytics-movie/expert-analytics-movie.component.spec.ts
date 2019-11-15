import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ExpertAnalyticsMovieComponent } from './expert-analytics-movie.component';

describe('ExpertAnalyticsMovieComponent', () => {
  let component: ExpertAnalyticsMovieComponent;
  let fixture: ComponentFixture<ExpertAnalyticsMovieComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ExpertAnalyticsMovieComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ExpertAnalyticsMovieComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
