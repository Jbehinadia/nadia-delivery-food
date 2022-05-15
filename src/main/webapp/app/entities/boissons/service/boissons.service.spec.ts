import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBoissons, Boissons } from '../boissons.model';

import { BoissonsService } from './boissons.service';

describe('Boissons Service', () => {
  let service: BoissonsService;
  let httpMock: HttpTestingController;
  let elemDefault: IBoissons;
  let expectedResult: IBoissons | IBoissons[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BoissonsService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nomBoissons: 'AAAAAAA',
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

    it('should create a Boissons', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Boissons()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Boissons', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomBoissons: 'BBBBBB',
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

    it('should partial update a Boissons', () => {
      const patchObject = Object.assign(
        {
          nomBoissons: 'BBBBBB',
          prix: 1,
          remisePerc: 1,
        },
        new Boissons()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Boissons', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomBoissons: 'BBBBBB',
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

    it('should delete a Boissons', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addBoissonsToCollectionIfMissing', () => {
      it('should add a Boissons to an empty array', () => {
        const boissons: IBoissons = { id: 123 };
        expectedResult = service.addBoissonsToCollectionIfMissing([], boissons);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(boissons);
      });

      it('should not add a Boissons to an array that contains it', () => {
        const boissons: IBoissons = { id: 123 };
        const boissonsCollection: IBoissons[] = [
          {
            ...boissons,
          },
          { id: 456 },
        ];
        expectedResult = service.addBoissonsToCollectionIfMissing(boissonsCollection, boissons);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Boissons to an array that doesn't contain it", () => {
        const boissons: IBoissons = { id: 123 };
        const boissonsCollection: IBoissons[] = [{ id: 456 }];
        expectedResult = service.addBoissonsToCollectionIfMissing(boissonsCollection, boissons);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(boissons);
      });

      it('should add only unique Boissons to an array', () => {
        const boissonsArray: IBoissons[] = [{ id: 123 }, { id: 456 }, { id: 71412 }];
        const boissonsCollection: IBoissons[] = [{ id: 123 }];
        expectedResult = service.addBoissonsToCollectionIfMissing(boissonsCollection, ...boissonsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const boissons: IBoissons = { id: 123 };
        const boissons2: IBoissons = { id: 456 };
        expectedResult = service.addBoissonsToCollectionIfMissing([], boissons, boissons2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(boissons);
        expect(expectedResult).toContain(boissons2);
      });

      it('should accept null and undefined values', () => {
        const boissons: IBoissons = { id: 123 };
        expectedResult = service.addBoissonsToCollectionIfMissing([], null, boissons, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(boissons);
      });

      it('should return initial array if no Boissons is added', () => {
        const boissonsCollection: IBoissons[] = [{ id: 123 }];
        expectedResult = service.addBoissonsToCollectionIfMissing(boissonsCollection, undefined, null);
        expect(expectedResult).toEqual(boissonsCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
