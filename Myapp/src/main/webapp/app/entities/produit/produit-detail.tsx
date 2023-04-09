import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './produit.reducer';

export const ProduitDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const produitEntity = useAppSelector(state => state.produit.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="produitDetailsHeading">
          <Translate contentKey="ghammazApp.produit.detail.title">Produit</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{produitEntity.id}</dd>
          <dt>
            <span id="idProd">
              <Translate contentKey="ghammazApp.produit.idProd">Id Prod</Translate>
            </span>
          </dt>
          <dd>{produitEntity.idProd}</dd>
          <dt>
            <span id="nomProd">
              <Translate contentKey="ghammazApp.produit.nomProd">Nom Prod</Translate>
            </span>
          </dt>
          <dd>{produitEntity.nomProd}</dd>
          <dt>
            <span id="prixProd">
              <Translate contentKey="ghammazApp.produit.prixProd">Prix Prod</Translate>
            </span>
          </dt>
          <dd>{produitEntity.prixProd}</dd>
          <dt>
            <Translate contentKey="ghammazApp.produit.livraison">Livraison</Translate>
          </dt>
          <dd>{produitEntity.livraison ? produitEntity.livraison.id : ''}</dd>
          <dt>
            <Translate contentKey="ghammazApp.produit.client">Client</Translate>
          </dt>
          <dd>{produitEntity.client ? produitEntity.client.id : ''}</dd>
          <dt>
            <Translate contentKey="ghammazApp.produit.restaurant">Restaurant</Translate>
          </dt>
          <dd>{produitEntity.restaurant ? produitEntity.restaurant.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/produit" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/produit/${produitEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProduitDetail;
