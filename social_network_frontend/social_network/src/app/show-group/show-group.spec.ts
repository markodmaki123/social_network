import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowGroup } from './show-group';

describe('ShowGroup', () => {
  let component: ShowGroup;
  let fixture: ComponentFixture<ShowGroup>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ShowGroup]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ShowGroup);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
