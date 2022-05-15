import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DessertComponent } from './list/dessert.component';
import { DessertDetailComponent } from './detail/dessert-detail.component';
import { DessertUpdateComponent } from './update/dessert-update.component';
import { DessertDeleteDialogComponent } from './delete/dessert-delete-dialog.component';
import { DessertRoutingModule } from './route/dessert-routing.module';

@NgModule({
  imports: [SharedModule, DessertRoutingModule],
  declarations: [DessertComponent, DessertDetailComponent, DessertUpdateComponent, DessertDeleteDialogComponent],
  entryComponents: [DessertDeleteDialogComponent],
})
export class DessertModule {}
