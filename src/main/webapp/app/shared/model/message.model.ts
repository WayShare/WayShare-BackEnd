import dayjs from 'dayjs';
import { IRide } from 'app/shared/model/ride.model';

export interface IMessage {
  id?: number;
  content?: string;
  timestamp?: dayjs.Dayjs;
  ride?: IRide | null;
}

export const defaultValue: Readonly<IMessage> = {};
