import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MedicalDomainSearchComponent } from './medical-domain-search.component';

describe('MedicalDomainSearchComponent', () => {
  let component: MedicalDomainSearchComponent;
  let fixture: ComponentFixture<MedicalDomainSearchComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MedicalDomainSearchComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MedicalDomainSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
