import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Association from './association';
import AssociationDetail from './association-detail';
import AssociationUpdate from './association-update';
import AssociationDeleteDialog from './association-delete-dialog';

const AssociationRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Association />} />
    <Route path="new" element={<AssociationUpdate />} />
    <Route path=":id">
      <Route index element={<AssociationDetail />} />
      <Route path="edit" element={<AssociationUpdate />} />
      <Route path="delete" element={<AssociationDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AssociationRoutes;
