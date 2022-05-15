import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBoissons, getBoissonsIdentifier } from '../boissons.model';

export type EntityResponseType = HttpResponse<IBoissons>;
export type EntityArrayResponseType = HttpResponse<IBoissons[]>;

@Injectable({ providedIn: 'root' })
export class BoissonsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/boissons');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(boissons: IBoissons): Observable<EntityResponseType> {
    return this.http.post<IBoissons>(this.resourceUrl, boissons, { observe: 'response' });
  }

  update(boissons: IBoissons): Observable<EntityResponseType> {
    return this.http.put<IBoissons>(`${this.resourceUrl}/${getBoissonsIdentifier(boissons) as number}`, boissons, { observe: 'response' });
  }

  partialUpdate(boissons: IBoissons): Observable<EntityResponseType> {
    return this.http.patch<IBoissons>(`${this.resourceUrl}/${getBoissonsIdentifier(boissons) as number}`, boissons, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBoissons>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBoissons[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addBoissonsToCollectionIfMissing(boissonsCollection: IBoissons[], ...boissonsToCheck: (IBoissons | null | undefined)[]): IBoissons[] {
    const boissons: IBoissons[] = boissonsToCheck.filter(isPresent);
    if (boissons.length > 0) {
      const boissonsCollectionIdentifiers = boissonsCollection.map(boissonsItem => getBoissonsIdentifier(boissonsItem)!);
      const boissonsToAdd = boissons.filter(boissonsItem => {
        const boissonsIdentifier = getBoissonsIdentifier(boissonsItem);
        if (boissonsIdentifier == null || boissonsCollectionIdentifiers.includes(boissonsIdentifier)) {
          return false;
        }
        boissonsCollectionIdentifiers.push(boissonsIdentifier);
        return true;
      });
      return [...boissonsToAdd, ...boissonsCollection];
    }
    return boissonsCollection;
  }
}
