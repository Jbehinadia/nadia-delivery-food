import { ICommandeDetails } from 'app/entities/commande-details/commande-details.model';
import { IMenu } from 'app/entities/menu/menu.model';

export interface IDessert {
  id?: number;
  idDessert?: string | null;
  nomDessert?: string | null;
  imagePath?: string | null;
  prix?: number | null;
  remisePerc?: number | null;
  remiceVal?: number | null;
  commandeDetails?: ICommandeDetails[] | null;
  menu?: IMenu | null;
}

export class Dessert implements IDessert {
  constructor(
    public id?: number,
    public idDessert?: string | null,
    public nomDessert?: string | null,
    public imagePath?: string | null,
    public prix?: number | null,
    public remisePerc?: number | null,
    public remiceVal?: number | null,
    public commandeDetails?: ICommandeDetails[] | null,
    public menu?: IMenu | null
  ) {}
}

export function getDessertIdentifier(dessert: IDessert): number | undefined {
  return dessert.id;
}
