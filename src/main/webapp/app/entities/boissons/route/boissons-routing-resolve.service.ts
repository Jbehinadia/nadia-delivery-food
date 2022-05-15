import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBoissons, Boissons } from '../boissons.model';
import { BoissonsService } from '../service/boissons.service';

@Injectable({ providedIn: 'root' })
export class BoissonsRoutingResolveService implements Resolve<IBoissons> {
  constructor(protected service: BoissonsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBoissons> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((boissons: HttpResponse<Boissons>) => {
          if (boissons.body) {
            return of(boissons.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Boissons());
  }
}
