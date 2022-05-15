import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDessert, Dessert } from '../dessert.model';
import { DessertService } from '../service/dessert.service';
import { IMenu } from 'app/entities/menu/menu.model';
import { MenuService } from 'app/entities/menu/service/menu.service';

@Component({
  selector: 'jhi-dessert-update',
  templateUrl: './dessert-update.component.html',
})
export class DessertUpdateComponent implements OnInit {
  isSaving = false;

  menusSharedCollection: IMenu[] = [];

  editForm = this.fb.group({
    id: [],
    nomDessert: [],
    imagePath: [],
    prix: [],
    remisePerc: [],
    remiceVal: [],
    menu: [],
  });

  constructor(
    protected dessertService: DessertService,
    protected menuService: MenuService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dessert }) => {
      this.updateForm(dessert);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const dessert = this.createFromForm();
    if (dessert.id !== undefined) {
      this.subscribeToSaveResponse(this.dessertService.update(dessert));
    } else {
      this.subscribeToSaveResponse(this.dessertService.create(dessert));
    }
  }

  trackMenuById(_index: number, item: IMenu): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDessert>>): void {
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

  protected updateForm(dessert: IDessert): void {
    this.editForm.patchValue({
      id: dessert.id,
      nomDessert: dessert.nomDessert,
      imagePath: dessert.imagePath,
      prix: dessert.prix,
      remisePerc: dessert.remisePerc,
      remiceVal: dessert.remiceVal,
      menu: dessert.menu,
    });

    this.menusSharedCollection = this.menuService.addMenuToCollectionIfMissing(this.menusSharedCollection, dessert.menu);
  }

  protected loadRelationshipsOptions(): void {
    this.menuService
      .query()
      .pipe(map((res: HttpResponse<IMenu[]>) => res.body ?? []))
      .pipe(map((menus: IMenu[]) => this.menuService.addMenuToCollectionIfMissing(menus, this.editForm.get('menu')!.value)))
      .subscribe((menus: IMenu[]) => (this.menusSharedCollection = menus));
  }

  protected createFromForm(): IDessert {
    return {
      ...new Dessert(),
      id: this.editForm.get(['id'])!.value,
      nomDessert: this.editForm.get(['nomDessert'])!.value,
      imagePath: this.editForm.get(['imagePath'])!.value,
      prix: this.editForm.get(['prix'])!.value,
      remisePerc: this.editForm.get(['remisePerc'])!.value,
      remiceVal: this.editForm.get(['remiceVal'])!.value,
      menu: this.editForm.get(['menu'])!.value,
    };
  }
}
