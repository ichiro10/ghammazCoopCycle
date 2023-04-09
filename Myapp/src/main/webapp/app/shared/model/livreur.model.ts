import { ILivraison } from 'app/shared/model/livraison.model';
import { IAssociation } from 'app/shared/model/association.model';

export interface ILivreur {
  id?: number;
  idLivreur?: number;
  nomLivreur?: string;
  prenomLivreur?: string;
  telLivreur?: string;
  livraisons?: ILivraison[] | null;
  association?: IAssociation | null;
}

export const defaultValue: Readonly<ILivreur> = {};
