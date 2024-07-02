import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import RideRequest from './ride-request';
import RideRequestDetail from './ride-request-detail';
import RideRequestUpdate from './ride-request-update';
import RideRequestDeleteDialog from './ride-request-delete-dialog';

const RideRequestRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<RideRequest />} />
    <Route path="new" element={<RideRequestUpdate />} />
    <Route path=":id">
      <Route index element={<RideRequestDetail />} />
      <Route path="edit" element={<RideRequestUpdate />} />
      <Route path="delete" element={<RideRequestDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default RideRequestRoutes;
