import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FastFoodComponent } from '../list/fast-food.component';
import { FastFoodDetailComponent } from '../detail/fast-food-detail.component';
import { FastFoodUpdateComponent } from '../update/fast-food-update.component';
import { FastFoodRoutingResolveService } from './fast-food-routing-resolve.service';

const fastFoodRoute: Routes = [
  {
    path: '',
    component: FastFoodComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FastFoodDetailComponent,
    resolve: {
      fastFood: FastFoodRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FastFoodUpdateComponent,
    resolve: {
      fastFood: FastFoodRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FastFoodUpdateComponent,
    resolve: {
      fastFood: FastFoodRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(fastFoodRoute)],
  exports: [RouterModule],
})
export class FastFoodRoutingModule {}
