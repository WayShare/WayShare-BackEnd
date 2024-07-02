import dayjs from 'dayjs';
import { IMember } from 'app/shared/model/member.model';

export interface IRide {
  id?: number;
  startLocation?: string;
  endLocation?: string;
  startTime?: dayjs.Dayjs;
  endTime?: dayjs.Dayjs | null;
  recurring?: boolean | null;
  member?: IMember | null;
}

export const defaultValue: Readonly<IRide> = {
  recurring: false,
};
