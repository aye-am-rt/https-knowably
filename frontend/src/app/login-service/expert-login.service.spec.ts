import { TestBed } from '@angular/core/testing';

import { ExpertLoginService } from './expert-login.service';

describe('ExpertLoginService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: ExpertLoginService = TestBed.get(ExpertLoginService);
    expect(service).toBeTruthy();
  });
});
