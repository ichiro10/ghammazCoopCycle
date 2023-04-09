import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Livraison from './livraison';
import LivraisonDetail from './livraison-detail';
import LivraisonUpdate from './livraison-update';
import LivraisonDeleteDialog from './livraison-delete-dialog';

const LivraisonRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Livraison />} />
    <Route path="new" element={<LivraisonUpdate />} />
    <Route path=":id">
      <Route index element={<LivraisonDetail />} />
      <Route path="edit" element={<LivraisonUpdate />} />
      <Route path="delete" element={<LivraisonDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default LivraisonRoutes;
