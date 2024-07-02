import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IMember } from 'app/shared/model/member.model';
import { getEntities as getMembers } from 'app/entities/member/member.reducer';
import { IRide } from 'app/shared/model/ride.model';
import { getEntity, updateEntity, createEntity, reset } from './ride.reducer';

export const RideUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const members = useAppSelector(state => state.member.entities);
  const rideEntity = useAppSelector(state => state.ride.entity);
  const loading = useAppSelector(state => state.ride.loading);
  const updating = useAppSelector(state => state.ride.updating);
  const updateSuccess = useAppSelector(state => state.ride.updateSuccess);

  const handleClose = () => {
    navigate('/ride');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getMembers({}));
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
    values.startTime = convertDateTimeToServer(values.startTime);
    values.endTime = convertDateTimeToServer(values.endTime);

    const entity = {
      ...rideEntity,
      ...values,
      member: members.find(it => it.id.toString() === values.member?.toString()),
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
          startTime: displayDefaultDateTime(),
          endTime: displayDefaultDateTime(),
        }
      : {
          ...rideEntity,
          startTime: convertDateTimeFromServer(rideEntity.startTime),
          endTime: convertDateTimeFromServer(rideEntity.endTime),
          member: rideEntity?.member?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="wayShareApp.ride.home.createOrEditLabel" data-cy="RideCreateUpdateHeading">
            <Translate contentKey="wayShareApp.ride.home.createOrEditLabel">Create or edit a Ride</Translate>
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
                  id="ride-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('wayShareApp.ride.startLocation')}
                id="ride-startLocation"
                name="startLocation"
                data-cy="startLocation"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('wayShareApp.ride.endLocation')}
                id="ride-endLocation"
                name="endLocation"
                data-cy="endLocation"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('wayShareApp.ride.startTime')}
                id="ride-startTime"
                name="startTime"
                data-cy="startTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('wayShareApp.ride.endTime')}
                id="ride-endTime"
                name="endTime"
                data-cy="endTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('wayShareApp.ride.recurring')}
                id="ride-recurring"
                name="recurring"
                data-cy="recurring"
                check
                type="checkbox"
              />
              <ValidatedField id="ride-member" name="member" data-cy="member" label={translate('wayShareApp.ride.member')} type="select">
                <option value="" key="0" />
                {members
                  ? members.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/ride" replace color="info">
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

export default RideUpdate;
