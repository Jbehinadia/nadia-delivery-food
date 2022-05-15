import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BoissonsDetailComponent } from './boissons-detail.component';

describe('Boissons Management Detail Component', () => {
  let comp: BoissonsDetailComponent;
  let fixture: ComponentFixture<BoissonsDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BoissonsDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ boissons: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(BoissonsDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(BoissonsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load boissons on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.boissons).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
