import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ILivraison } from 'app/shared/model/livraison.model';
import { getEntities } from './livraison.reducer';

export const Livraison = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const livraisonList = useAppSelector(state => state.livraison.entities);
  const loading = useAppSelector(state => state.livraison.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="livraison-heading" data-cy="LivraisonHeading">
        <Translate contentKey="ghammazApp.livraison.home.title">Livraisons</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="ghammazApp.livraison.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/livraison/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="ghammazApp.livraison.home.createLabel">Create new Livraison</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {livraisonList && livraisonList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="ghammazApp.livraison.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="ghammazApp.livraison.idLivraison">Id Livraison</Translate>
                </th>
                <th>
                  <Translate contentKey="ghammazApp.livraison.prixLivraison">Prix Livraison</Translate>
                </th>
                <th>
                  <Translate contentKey="ghammazApp.livraison.duree">Duree</Translate>
                </th>
                <th>
                  <Translate contentKey="ghammazApp.livraison.adresseLivraison">Adresse Livraison</Translate>
                </th>
                <th>
                  <Translate contentKey="ghammazApp.livraison.client">Client</Translate>
                </th>
                <th>
                  <Translate contentKey="ghammazApp.livraison.livreur">Livreur</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {livraisonList.map((livraison, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/livraison/${livraison.id}`} color="link" size="sm">
                      {livraison.id}
                    </Button>
                  </td>
                  <td>{livraison.idLivraison}</td>
                  <td>{livraison.prixLivraison}</td>
                  <td>{livraison.duree ? <TextFormat type="date" value={livraison.duree} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{livraison.adresseLivraison}</td>
                  <td>{livraison.client ? <Link to={`/client/${livraison.client.id}`}>{livraison.client.id}</Link> : ''}</td>
                  <td>{livraison.livreur ? <Link to={`/livreur/${livraison.livreur.id}`}>{livraison.livreur.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/livraison/${livraison.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/livraison/${livraison.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/livraison/${livraison.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="ghammazApp.livraison.home.notFound">No Livraisons found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Livraison;
