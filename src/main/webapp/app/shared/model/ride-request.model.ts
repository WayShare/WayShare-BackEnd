import dayjs from 'dayjs';
import { IRide } from 'app/shared/model/ride.model';

export interface IRideRequest {
  id?: number;
  status?: string;
  requestTime?: dayjs.Dayjs;
  ride?: IRide | null;
}

export const defaultValue: Readonly<IRideRequest> = {};
