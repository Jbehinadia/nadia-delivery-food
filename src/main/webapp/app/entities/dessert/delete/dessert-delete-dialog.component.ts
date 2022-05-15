import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDessert } from '../dessert.model';
import { DessertService } from '../service/dessert.service';

@Component({
  templateUrl: './dessert-delete-dialog.component.html',
})
export class DessertDeleteDialogComponent {
  dessert?: IDessert;

  constructor(protected dessertService: DessertService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.dessertService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
