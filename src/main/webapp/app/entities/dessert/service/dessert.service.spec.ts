import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDessert, Dessert } from '../dessert.model';

import { DessertService } from './dessert.service';

describe('Dessert Service', () => {
  let service: DessertService;
  let httpMock: HttpTestingController;
  let elemDefault: IDessert;
  let expectedResult: IDessert | IDessert[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DessertService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nomDessert: 'AAAAAAA',
      imagePath: 'AAAAAAA',
      prix: 0,
      remisePerc: 0,
      remiceVal: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Dessert', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Dessert()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Dessert', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomDessert: 'BBBBBB',
          imagePath: 'BBBBBB',
          prix: 1,
          remisePerc: 1,
          remiceVal: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Dessert', () => {
      const patchObject = Object.assign(
        {
          nomDessert: 'BBBBBB',
          prix: 1,
          remisePerc: 1,
        },
        new Dessert()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Dessert', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomDessert: 'BBBBBB',
          imagePath: 'BBBBBB',
          prix: 1,
          remisePerc: 1,
          remiceVal: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Dessert', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDessertToCollectionIfMissing', () => {
      it('should add a Dessert to an empty array', () => {
        const dessert: IDessert = { id: 123 };
        expectedResult = service.addDessertToCollectionIfMissing([], dessert);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(dessert);
      });

      it('should not add a Dessert to an array that contains it', () => {
        const dessert: IDessert = { id: 123 };
        const dessertCollection: IDessert[] = [
          {
            ...dessert,
          },
          { id: 456 },
        ];
        expectedResult = service.addDessertToCollectionIfMissing(dessertCollection, dessert);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Dessert to an array that doesn't contain it", () => {
        const dessert: IDessert = { id: 123 };
        const dessertCollection: IDessert[] = [{ id: 456 }];
        expectedResult = service.addDessertToCollectionIfMissing(dessertCollection, dessert);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(dessert);
      });

      it('should add only unique Dessert to an array', () => {
        const dessertArray: IDessert[] = [{ id: 123 }, { id: 456 }, { id: 50377 }];
        const dessertCollection: IDessert[] = [{ id: 123 }];
        expectedResult = service.addDessertToCollectionIfMissing(dessertCollection, ...dessertArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const dessert: IDessert = { id: 123 };
        const dessert2: IDessert = { id: 456 };
        expectedResult = service.addDessertToCollectionIfMissing([], dessert, dessert2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(dessert);
        expect(expectedResult).toContain(dessert2);
      });

      it('should accept null and undefined values', () => {
        const dessert: IDessert = { id: 123 };
        expectedResult = service.addDessertToCollectionIfMissing([], null, dessert, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(dessert);
      });

      it('should return initial array if no Dessert is added', () => {
        const dessertCollection: IDessert[] = [{ id: 123 }];
        expectedResult = service.addDessertToCollectionIfMissing(dessertCollection, undefined, null);
        expect(expectedResult).toEqual(dessertCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
