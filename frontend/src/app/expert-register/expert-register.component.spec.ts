import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ExpertRegisterComponent } from './expert-register.component';

describe('ExpertRegisterComponent', () => {
  let component: ExpertRegisterComponent;
  let fixture: ComponentFixture<ExpertRegisterComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ExpertRegisterComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ExpertRegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
