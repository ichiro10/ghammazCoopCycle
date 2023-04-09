import { ILivraison } from 'app/shared/model/livraison.model';
import { IClient } from 'app/shared/model/client.model';
import { IRestaurant } from 'app/shared/model/restaurant.model';

export interface IProduit {
  id?: number;
  idProd?: number;
  nomProd?: string;
  prixProd?: number;
  livraison?: ILivraison | null;
  client?: IClient | null;
  restaurant?: IRestaurant | null;
}

export const defaultValue: Readonly<IProduit> = {};
