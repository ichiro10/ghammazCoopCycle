import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './livreur.reducer';

export const LivreurDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const livreurEntity = useAppSelector(state => state.livreur.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="livreurDetailsHeading">
          <Translate contentKey="ghammazApp.livreur.detail.title">Livreur</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{livreurEntity.id}</dd>
          <dt>
            <span id="idLivreur">
              <Translate contentKey="ghammazApp.livreur.idLivreur">Id Livreur</Translate>
            </span>
          </dt>
          <dd>{livreurEntity.idLivreur}</dd>
          <dt>
            <span id="nomLivreur">
              <Translate contentKey="ghammazApp.livreur.nomLivreur">Nom Livreur</Translate>
            </span>
          </dt>
          <dd>{livreurEntity.nomLivreur}</dd>
          <dt>
            <span id="prenomLivreur">
              <Translate contentKey="ghammazApp.livreur.prenomLivreur">Prenom Livreur</Translate>
            </span>
          </dt>
          <dd>{livreurEntity.prenomLivreur}</dd>
          <dt>
            <span id="telLivreur">
              <Translate contentKey="ghammazApp.livreur.telLivreur">Tel Livreur</Translate>
            </span>
          </dt>
          <dd>{livreurEntity.telLivreur}</dd>
          <dt>
            <Translate contentKey="ghammazApp.livreur.association">Association</Translate>
          </dt>
          <dd>{livreurEntity.association ? livreurEntity.association.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/livreur" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/livreur/${livreurEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default LivreurDetail;
