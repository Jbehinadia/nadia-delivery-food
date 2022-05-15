import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPlat, Plat } from '../plat.model';
import { PlatService } from '../service/plat.service';
import { IMenu } from 'app/entities/menu/menu.model';
import { MenuService } from 'app/entities/menu/service/menu.service';

@Component({
  selector: 'jhi-plat-update',
  templateUrl: './plat-update.component.html',
})
export class PlatUpdateComponent implements OnInit {
  isSaving = false;

  menusSharedCollection: IMenu[] = [];

  editForm = this.fb.group({
    id: [],
    nomPlat: [],
    imagePath: [],
    prix: [],
    remisePerc: [],
    remiceVal: [],
    menu: [],
  });

  constructor(
    protected platService: PlatService,
    protected menuService: MenuService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ plat }) => {
      this.updateForm(plat);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const plat = this.createFromForm();
    if (plat.id !== undefined) {
      this.subscribeToSaveResponse(this.platService.update(plat));
    } else {
      this.subscribeToSaveResponse(this.platService.create(plat));
    }
  }

  trackMenuById(_index: number, item: IMenu): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlat>>): void {
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

  protected updateForm(plat: IPlat): void {
    this.editForm.patchValue({
      id: plat.id,
      nomPlat: plat.nomPlat,
      imagePath: plat.imagePath,
      prix: plat.prix,
      remisePerc: plat.remisePerc,
      remiceVal: plat.remiceVal,
      menu: plat.menu,
    });

    this.menusSharedCollection = this.menuService.addMenuToCollectionIfMissing(this.menusSharedCollection, plat.menu);
  }

  protected loadRelationshipsOptions(): void {
    this.menuService
      .query()
      .pipe(map((res: HttpResponse<IMenu[]>) => res.body ?? []))
      .pipe(map((menus: IMenu[]) => this.menuService.addMenuToCollectionIfMissing(menus, this.editForm.get('menu')!.value)))
      .subscribe((menus: IMenu[]) => (this.menusSharedCollection = menus));
  }

  protected createFromForm(): IPlat {
    return {
      ...new Plat(),
      id: this.editForm.get(['id'])!.value,
      nomPlat: this.editForm.get(['nomPlat'])!.value,
      imagePath: this.editForm.get(['imagePath'])!.value,
      prix: this.editForm.get(['prix'])!.value,
      remisePerc: this.editForm.get(['remisePerc'])!.value,
      remiceVal: this.editForm.get(['remiceVal'])!.value,
      menu: this.editForm.get(['menu'])!.value,
    };
  }
}
