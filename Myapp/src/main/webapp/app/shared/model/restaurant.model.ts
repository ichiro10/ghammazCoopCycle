import { IProduit } from 'app/shared/model/produit.model';

export interface IRestaurant {
  id?: number;
  idRest?: number;
  nomRest?: string;
  adresseRest?: string;
  produits?: IProduit[] | null;
}

export const defaultValue: Readonly<IRestaurant> = {};
