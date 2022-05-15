import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IFastFood, FastFood } from '../fast-food.model';
import { FastFoodService } from '../service/fast-food.service';
import { IMenu } from 'app/entities/menu/menu.model';
import { MenuService } from 'app/entities/menu/service/menu.service';

@Component({
  selector: 'jhi-fast-food-update',
  templateUrl: './fast-food-update.component.html',
})
export class FastFoodUpdateComponent implements OnInit {
  isSaving = false;

  menusSharedCollection: IMenu[] = [];

  editForm = this.fb.group({
    id: [],
    idFood: [],
    nomFood: [],
    imagePath: [],
    prix: [],
    remisePerc: [],
    remiceVal: [],
    menu: [],
  });

  constructor(
    protected fastFoodService: FastFoodService,
    protected menuService: MenuService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fastFood }) => {
      this.updateForm(fastFood);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const fastFood = this.createFromForm();
    if (fastFood.id !== undefined) {
      this.subscribeToSaveResponse(this.fastFoodService.update(fastFood));
    } else {
      this.subscribeToSaveResponse(this.fastFoodService.create(fastFood));
    }
  }

  trackMenuById(_index: number, item: IMenu): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFastFood>>): void {
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

  protected updateForm(fastFood: IFastFood): void {
    this.editForm.patchValue({
      id: fastFood.id,
      idFood: fastFood.idFood,
      nomFood: fastFood.nomFood,
      imagePath: fastFood.imagePath,
      prix: fastFood.prix,
      remisePerc: fastFood.remisePerc,
      remiceVal: fastFood.remiceVal,
      menu: fastFood.menu,
    });

    this.menusSharedCollection = this.menuService.addMenuToCollectionIfMissing(this.menusSharedCollection, fastFood.menu);
  }

  protected loadRelationshipsOptions(): void {
    this.menuService
      .query()
      .pipe(map((res: HttpResponse<IMenu[]>) => res.body ?? []))
      .pipe(map((menus: IMenu[]) => this.menuService.addMenuToCollectionIfMissing(menus, this.editForm.get('menu')!.value)))
      .subscribe((menus: IMenu[]) => (this.menusSharedCollection = menus));
  }

  protected createFromForm(): IFastFood {
    return {
      ...new FastFood(),
      id: this.editForm.get(['id'])!.value,
      idFood: this.editForm.get(['idFood'])!.value,
      nomFood: this.editForm.get(['nomFood'])!.value,
      imagePath: this.editForm.get(['imagePath'])!.value,
      prix: this.editForm.get(['prix'])!.value,
      remisePerc: this.editForm.get(['remisePerc'])!.value,
      remiceVal: this.editForm.get(['remiceVal'])!.value,
      menu: this.editForm.get(['menu'])!.value,
    };
  }
}
