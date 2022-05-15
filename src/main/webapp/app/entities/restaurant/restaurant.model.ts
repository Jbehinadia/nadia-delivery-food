import { IMenu } from 'app/entities/menu/menu.model';

export interface IRestaurant {
  id?: number;
  idRestaurant?: string | null;
  nomRestaurant?: string | null;
  adresseRestaurant?: string | null;
  numRestaurant?: string | null;
  commandes?: IMenu[] | null;
}

export class Restaurant implements IRestaurant {
  constructor(
    public id?: number,
    public idRestaurant?: string | null,
    public nomRestaurant?: string | null,
    public adresseRestaurant?: string | null,
    public numRestaurant?: string | null,
    public commandes?: IMenu[] | null
  ) {}
}

export function getRestaurantIdentifier(restaurant: IRestaurant): number | undefined {
  return restaurant.id;
}
