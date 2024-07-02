import dayjs from 'dayjs';
import { IMember } from 'app/shared/model/member.model';

export interface INotification {
  id?: number;
  message?: string;
  timestamp?: dayjs.Dayjs;
  read?: boolean | null;
  member?: IMember | null;
}

export const defaultValue: Readonly<INotification> = {
  read: false,
};
