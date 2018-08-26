import { TestBed, inject } from '@angular/core/testing';

import { UserDefinedServiceInstancesService } from './user-defined-service-instances.service';

describe('UserDefinedServiceInstancesService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [UserDefinedServiceInstancesService]
    });
  });

  it('should be created', inject([UserDefinedServiceInstancesService], (service: UserDefinedServiceInstancesService) => {
    expect(service).toBeTruthy();
  }));
});
