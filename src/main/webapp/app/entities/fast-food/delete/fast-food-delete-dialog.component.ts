import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFastFood } from '../fast-food.model';
import { FastFoodService } from '../service/fast-food.service';

@Component({
  templateUrl: './fast-food-delete-dialog.component.html',
})
export class FastFoodDeleteDialogComponent {
  fastFood?: IFastFood;

  constructor(protected fastFoodService: FastFoodService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.fastFoodService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
