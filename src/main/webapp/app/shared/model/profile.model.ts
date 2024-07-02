export interface IProfile {
  id?: number;
  firstName?: string | null;
  lastName?: string | null;
  photo?: string | null;
  contactDetails?: string | null;
}

export const defaultValue: Readonly<IProfile> = {};
