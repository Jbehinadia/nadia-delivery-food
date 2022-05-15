import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFastFood, getFastFoodIdentifier } from '../fast-food.model';

export type EntityResponseType = HttpResponse<IFastFood>;
export type EntityArrayResponseType = HttpResponse<IFastFood[]>;

@Injectable({ providedIn: 'root' })
export class FastFoodService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/fast-foods');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(fastFood: IFastFood): Observable<EntityResponseType> {
    return this.http.post<IFastFood>(this.resourceUrl, fastFood, { observe: 'response' });
  }

  update(fastFood: IFastFood): Observable<EntityResponseType> {
    return this.http.put<IFastFood>(`${this.resourceUrl}/${getFastFoodIdentifier(fastFood) as number}`, fastFood, { observe: 'response' });
  }

  partialUpdate(fastFood: IFastFood): Observable<EntityResponseType> {
    return this.http.patch<IFastFood>(`${this.resourceUrl}/${getFastFoodIdentifier(fastFood) as number}`, fastFood, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFastFood>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFastFood[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFastFoodToCollectionIfMissing(fastFoodCollection: IFastFood[], ...fastFoodsToCheck: (IFastFood | null | undefined)[]): IFastFood[] {
    const fastFoods: IFastFood[] = fastFoodsToCheck.filter(isPresent);
    if (fastFoods.length > 0) {
      const fastFoodCollectionIdentifiers = fastFoodCollection.map(fastFoodItem => getFastFoodIdentifier(fastFoodItem)!);
      const fastFoodsToAdd = fastFoods.filter(fastFoodItem => {
        const fastFoodIdentifier = getFastFoodIdentifier(fastFoodItem);
        if (fastFoodIdentifier == null || fastFoodCollectionIdentifiers.includes(fastFoodIdentifier)) {
          return false;
        }
        fastFoodCollectionIdentifiers.push(fastFoodIdentifier);
        return true;
      });
      return [...fastFoodsToAdd, ...fastFoodCollection];
    }
    return fastFoodCollection;
  }
}
