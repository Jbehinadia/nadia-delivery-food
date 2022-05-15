import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDessert, getDessertIdentifier } from '../dessert.model';

export type EntityResponseType = HttpResponse<IDessert>;
export type EntityArrayResponseType = HttpResponse<IDessert[]>;

@Injectable({ providedIn: 'root' })
export class DessertService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/desserts');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(dessert: IDessert): Observable<EntityResponseType> {
    return this.http.post<IDessert>(this.resourceUrl, dessert, { observe: 'response' });
  }

  update(dessert: IDessert): Observable<EntityResponseType> {
    return this.http.put<IDessert>(`${this.resourceUrl}/${getDessertIdentifier(dessert) as number}`, dessert, { observe: 'response' });
  }

  partialUpdate(dessert: IDessert): Observable<EntityResponseType> {
    return this.http.patch<IDessert>(`${this.resourceUrl}/${getDessertIdentifier(dessert) as number}`, dessert, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDessert>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDessert[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDessertToCollectionIfMissing(dessertCollection: IDessert[], ...dessertsToCheck: (IDessert | null | undefined)[]): IDessert[] {
    const desserts: IDessert[] = dessertsToCheck.filter(isPresent);
    if (desserts.length > 0) {
      const dessertCollectionIdentifiers = dessertCollection.map(dessertItem => getDessertIdentifier(dessertItem)!);
      const dessertsToAdd = desserts.filter(dessertItem => {
        const dessertIdentifier = getDessertIdentifier(dessertItem);
        if (dessertIdentifier == null || dessertCollectionIdentifiers.includes(dessertIdentifier)) {
          return false;
        }
        dessertCollectionIdentifiers.push(dessertIdentifier);
        return true;
      });
      return [...dessertsToAdd, ...dessertCollection];
    }
    return dessertCollection;
  }
}
