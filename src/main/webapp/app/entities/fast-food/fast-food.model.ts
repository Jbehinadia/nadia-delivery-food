import { ICommandeDetails } from 'app/entities/commande-details/commande-details.model';
import { IMenu } from 'app/entities/menu/menu.model';

export interface IFastFood {
  id?: number;
  nomFood?: string | null;
  imagePath?: string | null;
  prix?: number | null;
  remisePerc?: number | null;
  remiceVal?: number | null;
  commandeDetails?: ICommandeDetails[] | null;
  menu?: IMenu | null;
}

export class FastFood implements IFastFood {
  constructor(
    public id?: number,
    public nomFood?: string | null,
    public imagePath?: string | null,
    public prix?: number | null,
    public remisePerc?: number | null,
    public remiceVal?: number | null,
    public commandeDetails?: ICommandeDetails[] | null,
    public menu?: IMenu | null
  ) {}
}

export function getFastFoodIdentifier(fastFood: IFastFood): number | undefined {
  return fastFood.id;
}
