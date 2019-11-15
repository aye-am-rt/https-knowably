import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ExpertValidateDashboardComponent } from './expert-validate-dashboard.component';

describe('ExpertValidateDashboardComponent', () => {
  let component: ExpertValidateDashboardComponent;
  let fixture: ComponentFixture<ExpertValidateDashboardComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ExpertValidateDashboardComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ExpertValidateDashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
