import { ILivreur } from 'app/shared/model/livreur.model';

export interface IAssociation {
  id?: number;
  idAsso?: number;
  nomAsso?: string;
  livreurs?: ILivreur[] | null;
}

export const defaultValue: Readonly<IAssociation> = {};
