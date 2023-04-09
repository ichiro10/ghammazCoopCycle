import dayjs from 'dayjs';
import { IClient } from 'app/shared/model/client.model';
import { IProduit } from 'app/shared/model/produit.model';
import { ILivreur } from 'app/shared/model/livreur.model';

export interface ILivraison {
  id?: number;
  idLivraison?: number;
  prixLivraison?: number;
  duree?: string;
  adresseLivraison?: string;
  client?: IClient | null;
  produit?: IProduit | null;
  livreur?: ILivreur | null;
}

export const defaultValue: Readonly<ILivraison> = {};
