import { ICommandeDetails } from 'app/entities/commande-details/commande-details.model';
import { IMenu } from 'app/entities/menu/menu.model';

export interface IBoissons {
  id?: number;
  idBoissons?: string | null;
  nomBoissons?: string | null;
  imagePath?: string | null;
  prix?: number | null;
  remisePerc?: number | null;
  remiceVal?: number | null;
  commandeDetails?: ICommandeDetails[] | null;
  menu?: IMenu | null;
}

export class Boissons implements IBoissons {
  constructor(
    public id?: number,
    public idBoissons?: string | null,
    public nomBoissons?: string | null,
    public imagePath?: string | null,
    public prix?: number | null,
    public remisePerc?: number | null,
    public remiceVal?: number | null,
    public commandeDetails?: ICommandeDetails[] | null,
    public menu?: IMenu | null
  ) {}
}

export function getBoissonsIdentifier(boissons: IBoissons): number | undefined {
  return boissons.id;
}
