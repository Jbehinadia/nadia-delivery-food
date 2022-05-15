import { ICommande } from 'app/entities/commande/commande.model';
import { IFastFood } from 'app/entities/fast-food/fast-food.model';
import { IPlat } from 'app/entities/plat/plat.model';
import { IBoissons } from 'app/entities/boissons/boissons.model';
import { IDessert } from 'app/entities/dessert/dessert.model';

export interface ICommandeDetails {
  id?: number;
  prix?: number | null;
  etat?: string | null;
  commande?: ICommande | null;
  fastFood?: IFastFood | null;
  plat?: IPlat | null;
  boissons?: IBoissons | null;
  dessert?: IDessert | null;
}

export class CommandeDetails implements ICommandeDetails {
  constructor(
    public id?: number,
    public prix?: number | null,
    public etat?: string | null,
    public commande?: ICommande | null,
    public fastFood?: IFastFood | null,
    public plat?: IPlat | null,
    public boissons?: IBoissons | null,
    public dessert?: IDessert | null
  ) {}
}

export function getCommandeDetailsIdentifier(commandeDetails: ICommandeDetails): number | undefined {
  return commandeDetails.id;
}
