import { TestBed } from '@angular/core/testing';

import { MedicalSearchService } from './medical-search.service';

describe('MedicalSearchService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: MedicalSearchService = TestBed.get(MedicalSearchService);
    expect(service).toBeTruthy();
  });
});
