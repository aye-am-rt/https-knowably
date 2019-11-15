import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MovieDomainSearchComponent } from './movie-domain-search.component';

describe('MovieDomainSearchComponent', () => {
  let component: MovieDomainSearchComponent;
  let fixture: ComponentFixture<MovieDomainSearchComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MovieDomainSearchComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MovieDomainSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
