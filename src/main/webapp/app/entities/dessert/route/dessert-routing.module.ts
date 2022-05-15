import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DessertComponent } from '../list/dessert.component';
import { DessertDetailComponent } from '../detail/dessert-detail.component';
import { DessertUpdateComponent } from '../update/dessert-update.component';
import { DessertRoutingResolveService } from './dessert-routing-resolve.service';

const dessertRoute: Routes = [
  {
    path: '',
    component: DessertComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DessertDetailComponent,
    resolve: {
      dessert: DessertRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DessertUpdateComponent,
    resolve: {
      dessert: DessertRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DessertUpdateComponent,
    resolve: {
      dessert: DessertRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(dessertRoute)],
  exports: [RouterModule],
})
export class DessertRoutingModule {}
