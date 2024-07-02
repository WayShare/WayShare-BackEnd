import member from 'app/entities/member/member.reducer';
import profile from 'app/entities/profile/profile.reducer';
import ride from 'app/entities/ride/ride.reducer';
import rideRequest from 'app/entities/ride-request/ride-request.reducer';
import notification from 'app/entities/notification/notification.reducer';
import message from 'app/entities/message/message.reducer';
import rating from 'app/entities/rating/rating.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  member,
  profile,
  ride,
  rideRequest,
  notification,
  message,
  rating,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
