import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CommandeDetailsService } from '../service/commande-details.service';
import { ICommandeDetails, CommandeDetails } from '../commande-details.model';
import { ICommande } from 'app/entities/commande/commande.model';
import { CommandeService } from 'app/entities/commande/service/commande.service';
import { IFastFood } from 'app/entities/fast-food/fast-food.model';
import { FastFoodService } from 'app/entities/fast-food/service/fast-food.service';
import { IPlat } from 'app/entities/plat/plat.model';
import { PlatService } from 'app/entities/plat/service/plat.service';
import { IBoissons } from 'app/entities/boissons/boissons.model';
import { BoissonsService } from 'app/entities/boissons/service/boissons.service';
import { IDessert } from 'app/entities/dessert/dessert.model';
import { DessertService } from 'app/entities/dessert/service/dessert.service';

import { CommandeDetailsUpdateComponent } from './commande-details-update.component';

describe('CommandeDetails Management Update Component', () => {
  let comp: CommandeDetailsUpdateComponent;
  let fixture: ComponentFixture<CommandeDetailsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let commandeDetailsService: CommandeDetailsService;
  let commandeService: CommandeService;
  let fastFoodService: FastFoodService;
  let platService: PlatService;
  let boissonsService: BoissonsService;
  let dessertService: DessertService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CommandeDetailsUpdateComponent],
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
      .overrideTemplate(CommandeDetailsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CommandeDetailsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    commandeDetailsService = TestBed.inject(CommandeDetailsService);
    commandeService = TestBed.inject(CommandeService);
    fastFoodService = TestBed.inject(FastFoodService);
    platService = TestBed.inject(PlatService);
    boissonsService = TestBed.inject(BoissonsService);
    dessertService = TestBed.inject(DessertService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Commande query and add missing value', () => {
      const commandeDetails: ICommandeDetails = { id: 456 };
      const commande: ICommande = { id: 2572 };
      commandeDetails.commande = commande;

      const commandeCollection: ICommande[] = [{ id: 30085 }];
      jest.spyOn(commandeService, 'query').mockReturnValue(of(new HttpResponse({ body: commandeCollection })));
      const additionalCommandes = [commande];
      const expectedCollection: ICommande[] = [...additionalCommandes, ...commandeCollection];
      jest.spyOn(commandeService, 'addCommandeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ commandeDetails });
      comp.ngOnInit();

      expect(commandeService.query).toHaveBeenCalled();
      expect(commandeService.addCommandeToCollectionIfMissing).toHaveBeenCalledWith(commandeCollection, ...additionalCommandes);
      expect(comp.commandesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call FastFood query and add missing value', () => {
      const commandeDetails: ICommandeDetails = { id: 456 };
      const fastFood: IFastFood = { id: 44217 };
      commandeDetails.fastFood = fastFood;

      const fastFoodCollection: IFastFood[] = [{ id: 3263 }];
      jest.spyOn(fastFoodService, 'query').mockReturnValue(of(new HttpResponse({ body: fastFoodCollection })));
      const additionalFastFoods = [fastFood];
      const expectedCollection: IFastFood[] = [...additionalFastFoods, ...fastFoodCollection];
      jest.spyOn(fastFoodService, 'addFastFoodToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ commandeDetails });
      comp.ngOnInit();

      expect(fastFoodService.query).toHaveBeenCalled();
      expect(fastFoodService.addFastFoodToCollectionIfMissing).toHaveBeenCalledWith(fastFoodCollection, ...additionalFastFoods);
      expect(comp.fastFoodsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Plat query and add missing value', () => {
      const commandeDetails: ICommandeDetails = { id: 456 };
      const plat: IPlat = { id: 86021 };
      commandeDetails.plat = plat;

      const platCollection: IPlat[] = [{ id: 37903 }];
      jest.spyOn(platService, 'query').mockReturnValue(of(new HttpResponse({ body: platCollection })));
      const additionalPlats = [plat];
      const expectedCollection: IPlat[] = [...additionalPlats, ...platCollection];
      jest.spyOn(platService, 'addPlatToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ commandeDetails });
      comp.ngOnInit();

      expect(platService.query).toHaveBeenCalled();
      expect(platService.addPlatToCollectionIfMissing).toHaveBeenCalledWith(platCollection, ...additionalPlats);
      expect(comp.platsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Boissons query and add missing value', () => {
      const commandeDetails: ICommandeDetails = { id: 456 };
      const boissons: IBoissons = { id: 94345 };
      commandeDetails.boissons = boissons;

      const boissonsCollection: IBoissons[] = [{ id: 14954 }];
      jest.spyOn(boissonsService, 'query').mockReturnValue(of(new HttpResponse({ body: boissonsCollection })));
      const additionalBoissons = [boissons];
      const expectedCollection: IBoissons[] = [...additionalBoissons, ...boissonsCollection];
      jest.spyOn(boissonsService, 'addBoissonsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ commandeDetails });
      comp.ngOnInit();

      expect(boissonsService.query).toHaveBeenCalled();
      expect(boissonsService.addBoissonsToCollectionIfMissing).toHaveBeenCalledWith(boissonsCollection, ...additionalBoissons);
      expect(comp.boissonsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Dessert query and add missing value', () => {
      const commandeDetails: ICommandeDetails = { id: 456 };
      const dessert: IDessert = { id: 70352 };
      commandeDetails.dessert = dessert;

      const dessertCollection: IDessert[] = [{ id: 72489 }];
      jest.spyOn(dessertService, 'query').mockReturnValue(of(new HttpResponse({ body: dessertCollection })));
      const additionalDesserts = [dessert];
      const expectedCollection: IDessert[] = [...additionalDesserts, ...dessertCollection];
      jest.spyOn(dessertService, 'addDessertToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ commandeDetails });
      comp.ngOnInit();

      expect(dessertService.query).toHaveBeenCalled();
      expect(dessertService.addDessertToCollectionIfMissing).toHaveBeenCalledWith(dessertCollection, ...additionalDesserts);
      expect(comp.dessertsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const commandeDetails: ICommandeDetails = { id: 456 };
      const commande: ICommande = { id: 62288 };
      commandeDetails.commande = commande;
      const fastFood: IFastFood = { id: 41436 };
      commandeDetails.fastFood = fastFood;
      const plat: IPlat = { id: 50472 };
      commandeDetails.plat = plat;
      const boissons: IBoissons = { id: 80825 };
      commandeDetails.boissons = boissons;
      const dessert: IDessert = { id: 27662 };
      commandeDetails.dessert = dessert;

      activatedRoute.data = of({ commandeDetails });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(commandeDetails));
      expect(comp.commandesSharedCollection).toContain(commande);
      expect(comp.fastFoodsSharedCollection).toContain(fastFood);
      expect(comp.platsSharedCollection).toContain(plat);
      expect(comp.boissonsSharedCollection).toContain(boissons);
      expect(comp.dessertsSharedCollection).toContain(dessert);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CommandeDetails>>();
      const commandeDetails = { id: 123 };
      jest.spyOn(commandeDetailsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ commandeDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: commandeDetails }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(commandeDetailsService.update).toHaveBeenCalledWith(commandeDetails);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CommandeDetails>>();
      const commandeDetails = new CommandeDetails();
      jest.spyOn(commandeDetailsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ commandeDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: commandeDetails }));
      saveSubject.complete();

      // THEN
      expect(commandeDetailsService.create).toHaveBeenCalledWith(commandeDetails);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CommandeDetails>>();
      const commandeDetails = { id: 123 };
      jest.spyOn(commandeDetailsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ commandeDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(commandeDetailsService.update).toHaveBeenCalledWith(commandeDetails);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackCommandeById', () => {
      it('Should return tracked Commande primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCommandeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackFastFoodById', () => {
      it('Should return tracked FastFood primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackFastFoodById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackPlatById', () => {
      it('Should return tracked Plat primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPlatById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackBoissonsById', () => {
      it('Should return tracked Boissons primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackBoissonsById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackDessertById', () => {
      it('Should return tracked Dessert primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackDessertById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
