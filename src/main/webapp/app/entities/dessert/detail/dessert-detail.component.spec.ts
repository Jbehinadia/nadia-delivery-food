import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DessertDetailComponent } from './dessert-detail.component';

describe('Dessert Management Detail Component', () => {
  let comp: DessertDetailComponent;
  let fixture: ComponentFixture<DessertDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DessertDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ dessert: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DessertDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DessertDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load dessert on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.dessert).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
