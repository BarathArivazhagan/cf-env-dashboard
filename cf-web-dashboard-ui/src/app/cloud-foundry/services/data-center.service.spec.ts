import { TestBed, inject } from '@angular/core/testing';

import { DataCenterService } from './data-center.service';

describe('DataCenterService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [DataCenterService]
    });
  });

  it('should be created', inject([DataCenterService], (service: DataCenterService) => {
    expect(service).toBeTruthy();
  }));
});
