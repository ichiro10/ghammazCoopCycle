import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ILivraison } from 'app/shared/model/livraison.model';
import { getEntities as getLivraisons } from 'app/entities/livraison/livraison.reducer';
import { IClient } from 'app/shared/model/client.model';
import { getEntities as getClients } from 'app/entities/client/client.reducer';
import { IRestaurant } from 'app/shared/model/restaurant.model';
import { getEntities as getRestaurants } from 'app/entities/restaurant/restaurant.reducer';
import { IProduit } from 'app/shared/model/produit.model';
import { getEntity, updateEntity, createEntity, reset } from './produit.reducer';

export const ProduitUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const livraisons = useAppSelector(state => state.livraison.entities);
  const clients = useAppSelector(state => state.client.entities);
  const restaurants = useAppSelector(state => state.restaurant.entities);
  const produitEntity = useAppSelector(state => state.produit.entity);
  const loading = useAppSelector(state => state.produit.loading);
  const updating = useAppSelector(state => state.produit.updating);
  const updateSuccess = useAppSelector(state => state.produit.updateSuccess);

  const handleClose = () => {
    navigate('/produit');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getLivraisons({}));
    dispatch(getClients({}));
    dispatch(getRestaurants({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...produitEntity,
      ...values,
      livraison: livraisons.find(it => it.id.toString() === values.livraison.toString()),
      client: clients.find(it => it.id.toString() === values.client.toString()),
      restaurant: restaurants.find(it => it.id.toString() === values.restaurant.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...produitEntity,
          livraison: produitEntity?.livraison?.id,
          client: produitEntity?.client?.id,
          restaurant: produitEntity?.restaurant?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="ghammazApp.produit.home.createOrEditLabel" data-cy="ProduitCreateUpdateHeading">
            <Translate contentKey="ghammazApp.produit.home.createOrEditLabel">Create or edit a Produit</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="produit-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('ghammazApp.produit.idProd')}
                id="produit-idProd"
                name="idProd"
                data-cy="idProd"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('ghammazApp.produit.nomProd')}
                id="produit-nomProd"
                name="nomProd"
                data-cy="nomProd"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('ghammazApp.produit.prixProd')}
                id="produit-prixProd"
                name="prixProd"
                data-cy="prixProd"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                id="produit-livraison"
                name="livraison"
                data-cy="livraison"
                label={translate('ghammazApp.produit.livraison')}
                type="select"
              >
                <option value="" key="0" />
                {livraisons
                  ? livraisons.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="produit-client"
                name="client"
                data-cy="client"
                label={translate('ghammazApp.produit.client')}
                type="select"
              >
                <option value="" key="0" />
                {clients
                  ? clients.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="produit-restaurant"
                name="restaurant"
                data-cy="restaurant"
                label={translate('ghammazApp.produit.restaurant')}
                type="select"
              >
                <option value="" key="0" />
                {restaurants
                  ? restaurants.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/produit" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ProduitUpdate;
