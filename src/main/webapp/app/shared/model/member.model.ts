import { IProfile } from 'app/shared/model/profile.model';

export interface IMember {
  id?: number;
  login?: string;
  passwordHash?: string;
  email?: string;
  activated?: boolean | null;
  profile?: IProfile | null;
}

export const defaultValue: Readonly<IMember> = {
  activated: false,
};
