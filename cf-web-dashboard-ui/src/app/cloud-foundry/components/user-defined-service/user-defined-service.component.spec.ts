import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UserDefinedServiceComponent } from './user-defined-service.component';

describe('UserDefinedServiceComponent', () => {
  let component: UserDefinedServiceComponent;
  let fixture: ComponentFixture<UserDefinedServiceComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UserDefinedServiceComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UserDefinedServiceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
