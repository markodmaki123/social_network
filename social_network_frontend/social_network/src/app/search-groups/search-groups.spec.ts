import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchGroups } from './search-groups';

describe('SearchGroups', () => {
  let component: SearchGroups;
  let fixture: ComponentFixture<SearchGroups>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SearchGroups]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SearchGroups);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
