import { TestBed } from '@angular/core/testing';

import { AnalyticsGraphService } from './analytics-graph.service';

describe('AnalyticsGraphService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: AnalyticsGraphService = TestBed.get(AnalyticsGraphService);
    expect(service).toBeTruthy();
  });
});
