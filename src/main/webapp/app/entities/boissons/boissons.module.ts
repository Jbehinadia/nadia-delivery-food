import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { BoissonsComponent } from './list/boissons.component';
import { BoissonsDetailComponent } from './detail/boissons-detail.component';
import { BoissonsUpdateComponent } from './update/boissons-update.component';
import { BoissonsDeleteDialogComponent } from './delete/boissons-delete-dialog.component';
import { BoissonsRoutingModule } from './route/boissons-routing.module';

@NgModule({
  imports: [SharedModule, BoissonsRoutingModule],
  declarations: [BoissonsComponent, BoissonsDetailComponent, BoissonsUpdateComponent, BoissonsDeleteDialogComponent],
  entryComponents: [BoissonsDeleteDialogComponent],
})
export class BoissonsModule {}
