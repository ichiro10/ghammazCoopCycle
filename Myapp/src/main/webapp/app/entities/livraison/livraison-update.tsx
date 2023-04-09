import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IClient } from 'app/shared/model/client.model';
import { getEntities as getClients } from 'app/entities/client/client.reducer';
import { IProduit } from 'app/shared/model/produit.model';
import { getEntities as getProduits } from 'app/entities/produit/produit.reducer';
import { ILivreur } from 'app/shared/model/livreur.model';
import { getEntities as getLivreurs } from 'app/entities/livreur/livreur.reducer';
import { ILivraison } from 'app/shared/model/livraison.model';
import { getEntity, updateEntity, createEntity, reset } from './livraison.reducer';

export const LivraisonUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const clients = useAppSelector(state => state.client.entities);
  const produits = useAppSelector(state => state.produit.entities);
  const livreurs = useAppSelector(state => state.livreur.entities);
  const livraisonEntity = useAppSelector(state => state.livraison.entity);
  const loading = useAppSelector(state => state.livraison.loading);
  const updating = useAppSelector(state => state.livraison.updating);
  const updateSuccess = useAppSelector(state => state.livraison.updateSuccess);

  const handleClose = () => {
    navigate('/livraison');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getClients({}));
    dispatch(getProduits({}));
    dispatch(getLivreurs({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.duree = convertDateTimeToServer(values.duree);

    const entity = {
      ...livraisonEntity,
      ...values,
      client: clients.find(it => it.id.toString() === values.client.toString()),
      livreur: livreurs.find(it => it.id.toString() === values.livreur.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          duree: displayDefaultDateTime(),
        }
      : {
          ...livraisonEntity,
          duree: convertDateTimeFromServer(livraisonEntity.duree),
          client: livraisonEntity?.client?.id,
          livreur: livraisonEntity?.livreur?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="ghammazApp.livraison.home.createOrEditLabel" data-cy="LivraisonCreateUpdateHeading">
            <Translate contentKey="ghammazApp.livraison.home.createOrEditLabel">Create or edit a Livraison</Translate>
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
                  id="livraison-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('ghammazApp.livraison.idLivraison')}
                id="livraison-idLivraison"
                name="idLivraison"
                data-cy="idLivraison"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('ghammazApp.livraison.prixLivraison')}
                id="livraison-prixLivraison"
                name="prixLivraison"
                data-cy="prixLivraison"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('ghammazApp.livraison.duree')}
                id="livraison-duree"
                name="duree"
                data-cy="duree"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('ghammazApp.livraison.adresseLivraison')}
                id="livraison-adresseLivraison"
                name="adresseLivraison"
                data-cy="adresseLivraison"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="livraison-client"
                name="client"
                data-cy="client"
                label={translate('ghammazApp.livraison.client')}
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
                id="livraison-livreur"
                name="livreur"
                data-cy="livreur"
                label={translate('ghammazApp.livraison.livreur')}
                type="select"
              >
                <option value="" key="0" />
                {livreurs
                  ? livreurs.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/livraison" replace color="info">
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

export default LivraisonUpdate;
