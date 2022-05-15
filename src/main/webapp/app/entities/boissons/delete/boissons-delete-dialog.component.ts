import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBoissons } from '../boissons.model';
import { BoissonsService } from '../service/boissons.service';

@Component({
  templateUrl: './boissons-delete-dialog.component.html',
})
export class BoissonsDeleteDialogComponent {
  boissons?: IBoissons;

  constructor(protected boissonsService: BoissonsService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.boissonsService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
