import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BeaconComponent } from './beacon.component';

describe('BeaconComponent', () => {
  let component: BeaconComponent;
  let fixture: ComponentFixture<BeaconComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BeaconComponent]
    });
    fixture = TestBed.createComponent(BeaconComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
