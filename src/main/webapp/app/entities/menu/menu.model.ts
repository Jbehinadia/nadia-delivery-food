import { IFastFood } from 'app/entities/fast-food/fast-food.model';
import { IPlat } from 'app/entities/plat/plat.model';
import { IDessert } from 'app/entities/dessert/dessert.model';
import { IBoissons } from 'app/entities/boissons/boissons.model';
import { IRestaurant } from 'app/entities/restaurant/restaurant.model';

export interface IMenu {
  id?: number;
  nomMenu?: string | null;
  fastFoods?: IFastFood[] | null;
  plats?: IPlat[] | null;
  desserts?: IDessert[] | null;
  boissons?: IBoissons[] | null;
  restaurant?: IRestaurant | null;
}

export class Menu implements IMenu {
  constructor(
    public id?: number,
    public nomMenu?: string | null,
    public fastFoods?: IFastFood[] | null,
    public plats?: IPlat[] | null,
    public desserts?: IDessert[] | null,
    public boissons?: IBoissons[] | null,
    public restaurant?: IRestaurant | null
  ) {}
}

export function getMenuIdentifier(menu: IMenu): number | undefined {
  return menu.id;
}
