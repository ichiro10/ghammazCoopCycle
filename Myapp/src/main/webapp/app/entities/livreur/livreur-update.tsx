import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IAssociation } from 'app/shared/model/association.model';
import { getEntities as getAssociations } from 'app/entities/association/association.reducer';
import { ILivreur } from 'app/shared/model/livreur.model';
import { getEntity, updateEntity, createEntity, reset } from './livreur.reducer';

export const LivreurUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const associations = useAppSelector(state => state.association.entities);
  const livreurEntity = useAppSelector(state => state.livreur.entity);
  const loading = useAppSelector(state => state.livreur.loading);
  const updating = useAppSelector(state => state.livreur.updating);
  const updateSuccess = useAppSelector(state => state.livreur.updateSuccess);

  const handleClose = () => {
    navigate('/livreur');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getAssociations({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...livreurEntity,
      ...values,
      association: associations.find(it => it.id.toString() === values.association.toString()),
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
          ...livreurEntity,
          association: livreurEntity?.association?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="ghammazApp.livreur.home.createOrEditLabel" data-cy="LivreurCreateUpdateHeading">
            <Translate contentKey="ghammazApp.livreur.home.createOrEditLabel">Create or edit a Livreur</Translate>
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
                  id="livreur-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('ghammazApp.livreur.idLivreur')}
                id="livreur-idLivreur"
                name="idLivreur"
                data-cy="idLivreur"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('ghammazApp.livreur.nomLivreur')}
                id="livreur-nomLivreur"
                name="nomLivreur"
                data-cy="nomLivreur"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  minLength: { value: 2, message: translate('entity.validation.minlength', { min: 2 }) },
                }}
              />
              <ValidatedField
                label={translate('ghammazApp.livreur.prenomLivreur')}
                id="livreur-prenomLivreur"
                name="prenomLivreur"
                data-cy="prenomLivreur"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  minLength: { value: 2, message: translate('entity.validation.minlength', { min: 2 }) },
                }}
              />
              <ValidatedField
                label={translate('ghammazApp.livreur.telLivreur')}
                id="livreur-telLivreur"
                name="telLivreur"
                data-cy="telLivreur"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  pattern: { value: /(+\d+)?[0-9 ]+/, message: translate('entity.validation.pattern', { pattern: '(+\\d+)?[0-9 ]+' }) },
                }}
              />
              <ValidatedField
                id="livreur-association"
                name="association"
                data-cy="association"
                label={translate('ghammazApp.livreur.association')}
                type="select"
              >
                <option value="" key="0" />
                {associations
                  ? associations.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/livreur" replace color="info">
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

export default LivreurUpdate;
