import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { BoissonsService } from '../service/boissons.service';
import { IBoissons, Boissons } from '../boissons.model';
import { IMenu } from 'app/entities/menu/menu.model';
import { MenuService } from 'app/entities/menu/service/menu.service';

import { BoissonsUpdateComponent } from './boissons-update.component';

describe('Boissons Management Update Component', () => {
  let comp: BoissonsUpdateComponent;
  let fixture: ComponentFixture<BoissonsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let boissonsService: BoissonsService;
  let menuService: MenuService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [BoissonsUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(BoissonsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BoissonsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    boissonsService = TestBed.inject(BoissonsService);
    menuService = TestBed.inject(MenuService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Menu query and add missing value', () => {
      const boissons: IBoissons = { id: 456 };
      const menu: IMenu = { id: 61320 };
      boissons.menu = menu;

      const menuCollection: IMenu[] = [{ id: 36012 }];
      jest.spyOn(menuService, 'query').mockReturnValue(of(new HttpResponse({ body: menuCollection })));
      const additionalMenus = [menu];
      const expectedCollection: IMenu[] = [...additionalMenus, ...menuCollection];
      jest.spyOn(menuService, 'addMenuToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ boissons });
      comp.ngOnInit();

      expect(menuService.query).toHaveBeenCalled();
      expect(menuService.addMenuToCollectionIfMissing).toHaveBeenCalledWith(menuCollection, ...additionalMenus);
      expect(comp.menusSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const boissons: IBoissons = { id: 456 };
      const menu: IMenu = { id: 26000 };
      boissons.menu = menu;

      activatedRoute.data = of({ boissons });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(boissons));
      expect(comp.menusSharedCollection).toContain(menu);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Boissons>>();
      const boissons = { id: 123 };
      jest.spyOn(boissonsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ boissons });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: boissons }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(boissonsService.update).toHaveBeenCalledWith(boissons);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Boissons>>();
      const boissons = new Boissons();
      jest.spyOn(boissonsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ boissons });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: boissons }));
      saveSubject.complete();

      // THEN
      expect(boissonsService.create).toHaveBeenCalledWith(boissons);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Boissons>>();
      const boissons = { id: 123 };
      jest.spyOn(boissonsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ boissons });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(boissonsService.update).toHaveBeenCalledWith(boissons);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackMenuById', () => {
      it('Should return tracked Menu primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackMenuById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
