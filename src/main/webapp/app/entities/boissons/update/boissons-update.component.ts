import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IBoissons, Boissons } from '../boissons.model';
import { BoissonsService } from '../service/boissons.service';
import { IMenu } from 'app/entities/menu/menu.model';
import { MenuService } from 'app/entities/menu/service/menu.service';

@Component({
  selector: 'jhi-boissons-update',
  templateUrl: './boissons-update.component.html',
})
export class BoissonsUpdateComponent implements OnInit {
  isSaving = false;

  menusSharedCollection: IMenu[] = [];

  editForm = this.fb.group({
    id: [],
    idBoissons: [],
    nomBoissons: [],
    imagePath: [],
    prix: [],
    remisePerc: [],
    remiceVal: [],
    menu: [],
  });

  constructor(
    protected boissonsService: BoissonsService,
    protected menuService: MenuService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ boissons }) => {
      this.updateForm(boissons);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const boissons = this.createFromForm();
    if (boissons.id !== undefined) {
      this.subscribeToSaveResponse(this.boissonsService.update(boissons));
    } else {
      this.subscribeToSaveResponse(this.boissonsService.create(boissons));
    }
  }

  trackMenuById(_index: number, item: IMenu): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBoissons>>): void {
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

  protected updateForm(boissons: IBoissons): void {
    this.editForm.patchValue({
      id: boissons.id,
      idBoissons: boissons.idBoissons,
      nomBoissons: boissons.nomBoissons,
      imagePath: boissons.imagePath,
      prix: boissons.prix,
      remisePerc: boissons.remisePerc,
      remiceVal: boissons.remiceVal,
      menu: boissons.menu,
    });

    this.menusSharedCollection = this.menuService.addMenuToCollectionIfMissing(this.menusSharedCollection, boissons.menu);
  }

  protected loadRelationshipsOptions(): void {
    this.menuService
      .query()
      .pipe(map((res: HttpResponse<IMenu[]>) => res.body ?? []))
      .pipe(map((menus: IMenu[]) => this.menuService.addMenuToCollectionIfMissing(menus, this.editForm.get('menu')!.value)))
      .subscribe((menus: IMenu[]) => (this.menusSharedCollection = menus));
  }

  protected createFromForm(): IBoissons {
    return {
      ...new Boissons(),
      id: this.editForm.get(['id'])!.value,
      idBoissons: this.editForm.get(['idBoissons'])!.value,
      nomBoissons: this.editForm.get(['nomBoissons'])!.value,
      imagePath: this.editForm.get(['imagePath'])!.value,
      prix: this.editForm.get(['prix'])!.value,
      remisePerc: this.editForm.get(['remisePerc'])!.value,
      remiceVal: this.editForm.get(['remiceVal'])!.value,
      menu: this.editForm.get(['menu'])!.value,
    };
  }
}
