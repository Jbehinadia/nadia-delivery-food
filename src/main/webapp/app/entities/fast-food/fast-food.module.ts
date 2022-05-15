import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FastFoodComponent } from './list/fast-food.component';
import { FastFoodDetailComponent } from './detail/fast-food-detail.component';
import { FastFoodUpdateComponent } from './update/fast-food-update.component';
import { FastFoodDeleteDialogComponent } from './delete/fast-food-delete-dialog.component';
import { FastFoodRoutingModule } from './route/fast-food-routing.module';

@NgModule({
  imports: [SharedModule, FastFoodRoutingModule],
  declarations: [FastFoodComponent, FastFoodDetailComponent, FastFoodUpdateComponent, FastFoodDeleteDialogComponent],
  entryComponents: [FastFoodDeleteDialogComponent],
})
export class FastFoodModule {}
