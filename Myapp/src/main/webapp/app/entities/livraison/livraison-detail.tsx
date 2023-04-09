import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './livraison.reducer';

export const LivraisonDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const livraisonEntity = useAppSelector(state => state.livraison.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="livraisonDetailsHeading">
          <Translate contentKey="ghammazApp.livraison.detail.title">Livraison</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{livraisonEntity.id}</dd>
          <dt>
            <span id="idLivraison">
              <Translate contentKey="ghammazApp.livraison.idLivraison">Id Livraison</Translate>
            </span>
          </dt>
          <dd>{livraisonEntity.idLivraison}</dd>
          <dt>
            <span id="prixLivraison">
              <Translate contentKey="ghammazApp.livraison.prixLivraison">Prix Livraison</Translate>
            </span>
          </dt>
          <dd>{livraisonEntity.prixLivraison}</dd>
          <dt>
            <span id="duree">
              <Translate contentKey="ghammazApp.livraison.duree">Duree</Translate>
            </span>
          </dt>
          <dd>{livraisonEntity.duree ? <TextFormat value={livraisonEntity.duree} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="adresseLivraison">
              <Translate contentKey="ghammazApp.livraison.adresseLivraison">Adresse Livraison</Translate>
            </span>
          </dt>
          <dd>{livraisonEntity.adresseLivraison}</dd>
          <dt>
            <Translate contentKey="ghammazApp.livraison.client">Client</Translate>
          </dt>
          <dd>{livraisonEntity.client ? livraisonEntity.client.id : ''}</dd>
          <dt>
            <Translate contentKey="ghammazApp.livraison.livreur">Livreur</Translate>
          </dt>
          <dd>{livraisonEntity.livreur ? livraisonEntity.livreur.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/livraison" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/livraison/${livraisonEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default LivraisonDetail;
