import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './ride.reducer';

export const RideDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const rideEntity = useAppSelector(state => state.ride.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="rideDetailsHeading">
          <Translate contentKey="wayShareApp.ride.detail.title">Ride</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{rideEntity.id}</dd>
          <dt>
            <span id="startLocation">
              <Translate contentKey="wayShareApp.ride.startLocation">Start Location</Translate>
            </span>
          </dt>
          <dd>{rideEntity.startLocation}</dd>
          <dt>
            <span id="endLocation">
              <Translate contentKey="wayShareApp.ride.endLocation">End Location</Translate>
            </span>
          </dt>
          <dd>{rideEntity.endLocation}</dd>
          <dt>
            <span id="startTime">
              <Translate contentKey="wayShareApp.ride.startTime">Start Time</Translate>
            </span>
          </dt>
          <dd>{rideEntity.startTime ? <TextFormat value={rideEntity.startTime} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="endTime">
              <Translate contentKey="wayShareApp.ride.endTime">End Time</Translate>
            </span>
          </dt>
          <dd>{rideEntity.endTime ? <TextFormat value={rideEntity.endTime} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="recurring">
              <Translate contentKey="wayShareApp.ride.recurring">Recurring</Translate>
            </span>
          </dt>
          <dd>{rideEntity.recurring ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="wayShareApp.ride.member">Member</Translate>
          </dt>
          <dd>{rideEntity.member ? rideEntity.member.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/ride" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/ride/${rideEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default RideDetail;
