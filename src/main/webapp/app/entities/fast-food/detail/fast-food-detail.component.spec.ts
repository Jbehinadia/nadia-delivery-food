import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FastFoodDetailComponent } from './fast-food-detail.component';

describe('FastFood Management Detail Component', () => {
  let comp: FastFoodDetailComponent;
  let fixture: ComponentFixture<FastFoodDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FastFoodDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ fastFood: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(FastFoodDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(FastFoodDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load fastFood on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.fastFood).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
