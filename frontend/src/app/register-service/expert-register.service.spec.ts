import { TestBed } from '@angular/core/testing';

import { ExpertRegisterService } from './expert-register.service';

describe('ExpertRegisterService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: ExpertRegisterService = TestBed.get(ExpertRegisterService);
    expect(service).toBeTruthy();
  });
});
