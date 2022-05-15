import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICommandeDetails, CommandeDetails } from '../commande-details.model';
import { CommandeDetailsService } from '../service/commande-details.service';
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

@Component({
  selector: 'jhi-commande-details-update',
  templateUrl: './commande-details-update.component.html',
})
export class CommandeDetailsUpdateComponent implements OnInit {
  isSaving = false;

  commandesSharedCollection: ICommande[] = [];
  fastFoodsSharedCollection: IFastFood[] = [];
  platsSharedCollection: IPlat[] = [];
  boissonsSharedCollection: IBoissons[] = [];
  dessertsSharedCollection: IDessert[] = [];

  editForm = this.fb.group({
    id: [],
    prix: [],
    etat: [],
    commande: [],
    fastFood: [],
    plat: [],
    boissons: [],
    dessert: [],
  });

  constructor(
    protected commandeDetailsService: CommandeDetailsService,
    protected commandeService: CommandeService,
    protected fastFoodService: FastFoodService,
    protected platService: PlatService,
    protected boissonsService: BoissonsService,
    protected dessertService: DessertService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ commandeDetails }) => {
      this.updateForm(commandeDetails);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const commandeDetails = this.createFromForm();
    if (commandeDetails.id !== undefined) {
      this.subscribeToSaveResponse(this.commandeDetailsService.update(commandeDetails));
    } else {
      this.subscribeToSaveResponse(this.commandeDetailsService.create(commandeDetails));
    }
  }

  trackCommandeById(_index: number, item: ICommande): number {
    return item.id!;
  }

  trackFastFoodById(_index: number, item: IFastFood): number {
    return item.id!;
  }

  trackPlatById(_index: number, item: IPlat): number {
    return item.id!;
  }

  trackBoissonsById(_index: number, item: IBoissons): number {
    return item.id!;
  }

  trackDessertById(_index: number, item: IDessert): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICommandeDetails>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(commandeDetails: ICommandeDetails): void {
    this.editForm.patchValue({
      id: commandeDetails.id,
      prix: commandeDetails.prix,
      etat: commandeDetails.etat,
      commande: commandeDetails.commande,
      fastFood: commandeDetails.fastFood,
      plat: commandeDetails.plat,
      boissons: commandeDetails.boissons,
      dessert: commandeDetails.dessert,
    });

    this.commandesSharedCollection = this.commandeService.addCommandeToCollectionIfMissing(
      this.commandesSharedCollection,
      commandeDetails.commande
    );
    this.fastFoodsSharedCollection = this.fastFoodService.addFastFoodToCollectionIfMissing(
      this.fastFoodsSharedCollection,
      commandeDetails.fastFood
    );
    this.platsSharedCollection = this.platService.addPlatToCollectionIfMissing(this.platsSharedCollection, commandeDetails.plat);
    this.boissonsSharedCollection = this.boissonsService.addBoissonsToCollectionIfMissing(
      this.boissonsSharedCollection,
      commandeDetails.boissons
    );
    this.dessertsSharedCollection = this.dessertService.addDessertToCollectionIfMissing(
      this.dessertsSharedCollection,
      commandeDetails.dessert
    );
  }

  protected loadRelationshipsOptions(): void {
    this.commandeService
      .query()
      .pipe(map((res: HttpResponse<ICommande[]>) => res.body ?? []))
      .pipe(
        map((commandes: ICommande[]) =>
          this.commandeService.addCommandeToCollectionIfMissing(commandes, this.editForm.get('commande')!.value)
        )
      )
      .subscribe((commandes: ICommande[]) => (this.commandesSharedCollection = commandes));

    this.fastFoodService
      .query()
      .pipe(map((res: HttpResponse<IFastFood[]>) => res.body ?? []))
      .pipe(
        map((fastFoods: IFastFood[]) =>
          this.fastFoodService.addFastFoodToCollectionIfMissing(fastFoods, this.editForm.get('fastFood')!.value)
        )
      )
      .subscribe((fastFoods: IFastFood[]) => (this.fastFoodsSharedCollection = fastFoods));

    this.platService
      .query()
      .pipe(map((res: HttpResponse<IPlat[]>) => res.body ?? []))
      .pipe(map((plats: IPlat[]) => this.platService.addPlatToCollectionIfMissing(plats, this.editForm.get('plat')!.value)))
      .subscribe((plats: IPlat[]) => (this.platsSharedCollection = plats));

    this.boissonsService
      .query()
      .pipe(map((res: HttpResponse<IBoissons[]>) => res.body ?? []))
      .pipe(
        map((boissons: IBoissons[]) =>
          this.boissonsService.addBoissonsToCollectionIfMissing(boissons, this.editForm.get('boissons')!.value)
        )
      )
      .subscribe((boissons: IBoissons[]) => (this.boissonsSharedCollection = boissons));

    this.dessertService
      .query()
      .pipe(map((res: HttpResponse<IDessert[]>) => res.body ?? []))
      .pipe(
        map((desserts: IDessert[]) => this.dessertService.addDessertToCollectionIfMissing(desserts, this.editForm.get('dessert')!.value))
      )
      .subscribe((desserts: IDessert[]) => (this.dessertsSharedCollection = desserts));
  }

  protected createFromForm(): ICommandeDetails {
    return {
      ...new CommandeDetails(),
      id: this.editForm.get(['id'])!.value,
      prix: this.editForm.get(['prix'])!.value,
      etat: this.editForm.get(['etat'])!.value,
      commande: this.editForm.get(['commande'])!.value,
      fastFood: this.editForm.get(['fastFood'])!.value,
      plat: this.editForm.get(['plat'])!.value,
      boissons: this.editForm.get(['boissons'])!.value,
      dessert: this.editForm.get(['dessert'])!.value,
    };
  }
}
