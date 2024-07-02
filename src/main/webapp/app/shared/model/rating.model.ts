import { IMember } from 'app/shared/model/member.model';

export interface IRating {
  id?: number;
  score?: number;
  feedback?: string | null;
  giver?: IMember | null;
  receiver?: IMember | null;
}

export const defaultValue: Readonly<IRating> = {};
