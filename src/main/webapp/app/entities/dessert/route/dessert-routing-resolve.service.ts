import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDessert, Dessert } from '../dessert.model';
import { DessertService } from '../service/dessert.service';

@Injectable({ providedIn: 'root' })
export class DessertRoutingResolveService implements Resolve<IDessert> {
  constructor(protected service: DessertService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDessert> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((dessert: HttpResponse<Dessert>) => {
          if (dessert.body) {
            return of(dessert.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Dessert());
  }
}
