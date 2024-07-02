import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Member from './member';
import Profile from './profile';
import Ride from './ride';
import RideRequest from './ride-request';
import Notification from './notification';
import Message from './message';
import Rating from './rating';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="member/*" element={<Member />} />
        <Route path="profile/*" element={<Profile />} />
        <Route path="ride/*" element={<Ride />} />
        <Route path="ride-request/*" element={<RideRequest />} />
        <Route path="notification/*" element={<Notification />} />
        <Route path="message/*" element={<Message />} />
        <Route path="rating/*" element={<Rating />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
