import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BoissonsComponent } from '../list/boissons.component';
import { BoissonsDetailComponent } from '../detail/boissons-detail.component';
import { BoissonsUpdateComponent } from '../update/boissons-update.component';
import { BoissonsRoutingResolveService } from './boissons-routing-resolve.service';

const boissonsRoute: Routes = [
  {
    path: '',
    component: BoissonsComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BoissonsDetailComponent,
    resolve: {
      boissons: BoissonsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BoissonsUpdateComponent,
    resolve: {
      boissons: BoissonsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BoissonsUpdateComponent,
    resolve: {
      boissons: BoissonsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(boissonsRoute)],
  exports: [RouterModule],
})
export class BoissonsRoutingModule {}
