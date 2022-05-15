import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IFastFood, FastFood } from '../fast-food.model';

import { FastFoodService } from './fast-food.service';

describe('FastFood Service', () => {
  let service: FastFoodService;
  let httpMock: HttpTestingController;
  let elemDefault: IFastFood;
  let expectedResult: IFastFood | IFastFood[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FastFoodService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      idFood: 'AAAAAAA',
      nomFood: 'AAAAAAA',
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

    it('should create a FastFood', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new FastFood()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FastFood', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          idFood: 'BBBBBB',
          nomFood: 'BBBBBB',
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

    it('should partial update a FastFood', () => {
      const patchObject = Object.assign(
        {
          idFood: 'BBBBBB',
          nomFood: 'BBBBBB',
          prix: 1,
          remiceVal: 1,
        },
        new FastFood()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FastFood', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          idFood: 'BBBBBB',
          nomFood: 'BBBBBB',
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

    it('should delete a FastFood', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addFastFoodToCollectionIfMissing', () => {
      it('should add a FastFood to an empty array', () => {
        const fastFood: IFastFood = { id: 123 };
        expectedResult = service.addFastFoodToCollectionIfMissing([], fastFood);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fastFood);
      });

      it('should not add a FastFood to an array that contains it', () => {
        const fastFood: IFastFood = { id: 123 };
        const fastFoodCollection: IFastFood[] = [
          {
            ...fastFood,
          },
          { id: 456 },
        ];
        expectedResult = service.addFastFoodToCollectionIfMissing(fastFoodCollection, fastFood);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FastFood to an array that doesn't contain it", () => {
        const fastFood: IFastFood = { id: 123 };
        const fastFoodCollection: IFastFood[] = [{ id: 456 }];
        expectedResult = service.addFastFoodToCollectionIfMissing(fastFoodCollection, fastFood);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fastFood);
      });

      it('should add only unique FastFood to an array', () => {
        const fastFoodArray: IFastFood[] = [{ id: 123 }, { id: 456 }, { id: 6729 }];
        const fastFoodCollection: IFastFood[] = [{ id: 123 }];
        expectedResult = service.addFastFoodToCollectionIfMissing(fastFoodCollection, ...fastFoodArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const fastFood: IFastFood = { id: 123 };
        const fastFood2: IFastFood = { id: 456 };
        expectedResult = service.addFastFoodToCollectionIfMissing([], fastFood, fastFood2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fastFood);
        expect(expectedResult).toContain(fastFood2);
      });

      it('should accept null and undefined values', () => {
        const fastFood: IFastFood = { id: 123 };
        expectedResult = service.addFastFoodToCollectionIfMissing([], null, fastFood, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fastFood);
      });

      it('should return initial array if no FastFood is added', () => {
        const fastFoodCollection: IFastFood[] = [{ id: 123 }];
        expectedResult = service.addFastFoodToCollectionIfMissing(fastFoodCollection, undefined, null);
        expect(expectedResult).toEqual(fastFoodCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
