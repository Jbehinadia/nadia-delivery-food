import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFastFood, FastFood } from '../fast-food.model';
import { FastFoodService } from '../service/fast-food.service';

@Injectable({ providedIn: 'root' })
export class FastFoodRoutingResolveService implements Resolve<IFastFood> {
  constructor(protected service: FastFoodService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFastFood> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((fastFood: HttpResponse<FastFood>) => {
          if (fastFood.body) {
            return of(fastFood.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new FastFood());
  }
}
