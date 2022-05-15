import { ICommandeDetails } from 'app/entities/commande-details/commande-details.model';
import { IMenu } from 'app/entities/menu/menu.model';

export interface IPlat {
  id?: number;
  idPlat?: string | null;
  nomPlat?: string | null;
  imagePath?: string | null;
  prix?: number | null;
  remisePerc?: number | null;
  remiceVal?: number | null;
  commandeDetails?: ICommandeDetails[] | null;
  menu?: IMenu | null;
}

export class Plat implements IPlat {
  constructor(
    public id?: number,
    public idPlat?: string | null,
    public nomPlat?: string | null,
    public imagePath?: string | null,
    public prix?: number | null,
    public remisePerc?: number | null,
    public remiceVal?: number | null,
    public commandeDetails?: ICommandeDetails[] | null,
    public menu?: IMenu | null
  ) {}
}

export function getPlatIdentifier(plat: IPlat): number | undefined {
  return plat.id;
}
