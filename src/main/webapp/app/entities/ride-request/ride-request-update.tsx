import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IRide } from 'app/shared/model/ride.model';
import { getEntities as getRides } from 'app/entities/ride/ride.reducer';
import { IRideRequest } from 'app/shared/model/ride-request.model';
import { getEntity, updateEntity, createEntity, reset } from './ride-request.reducer';

export const RideRequestUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const rides = useAppSelector(state => state.ride.entities);
  const rideRequestEntity = useAppSelector(state => state.rideRequest.entity);
  const loading = useAppSelector(state => state.rideRequest.loading);
  const updating = useAppSelector(state => state.rideRequest.updating);
  const updateSuccess = useAppSelector(state => state.rideRequest.updateSuccess);

  const handleClose = () => {
    navigate('/ride-request');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(id));
    }

    dispatch(getRides({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    values.requestTime = convertDateTimeToServer(values.requestTime);

    const entity = {
      ...rideRequestEntity,
      ...values,
      ride: rides.find(it => it.id.toString() === values.ride?.toString()),
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
          requestTime: displayDefaultDateTime(),
        }
      : {
          ...rideRequestEntity,
          requestTime: convertDateTimeFromServer(rideRequestEntity.requestTime),
          ride: rideRequestEntity?.ride?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="wayShareApp.rideRequest.home.createOrEditLabel" data-cy="RideRequestCreateUpdateHeading">
            <Translate contentKey="wayShareApp.rideRequest.home.createOrEditLabel">Create or edit a RideRequest</Translate>
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
                  id="ride-request-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('wayShareApp.rideRequest.status')}
                id="ride-request-status"
                name="status"
                data-cy="status"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('wayShareApp.rideRequest.requestTime')}
                id="ride-request-requestTime"
                name="requestTime"
                data-cy="requestTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="ride-request-ride"
                name="ride"
                data-cy="ride"
                label={translate('wayShareApp.rideRequest.ride')}
                type="select"
              >
                <option value="" key="0" />
                {rides
                  ? rides.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/ride-request" replace color="info">
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

export default RideRequestUpdate;
