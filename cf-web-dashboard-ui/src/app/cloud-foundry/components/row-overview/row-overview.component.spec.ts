import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RowOverviewComponent } from './row-overview.component';

describe('RowOverviewComponent', () => {
  let component: RowOverviewComponent;
  let fixture: ComponentFixture<RowOverviewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RowOverviewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RowOverviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
