import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IAssociation } from 'app/shared/model/association.model';
import { getEntities } from './association.reducer';

export const Association = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const associationList = useAppSelector(state => state.association.entities);
  const loading = useAppSelector(state => state.association.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="association-heading" data-cy="AssociationHeading">
        <Translate contentKey="ghammazApp.association.home.title">Associations</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="ghammazApp.association.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/association/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="ghammazApp.association.home.createLabel">Create new Association</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {associationList && associationList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="ghammazApp.association.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="ghammazApp.association.idAsso">Id Asso</Translate>
                </th>
                <th>
                  <Translate contentKey="ghammazApp.association.nomAsso">Nom Asso</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {associationList.map((association, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/association/${association.id}`} color="link" size="sm">
                      {association.id}
                    </Button>
                  </td>
                  <td>{association.idAsso}</td>
                  <td>{association.nomAsso}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/association/${association.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/association/${association.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/association/${association.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="ghammazApp.association.home.notFound">No Associations found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Association;
