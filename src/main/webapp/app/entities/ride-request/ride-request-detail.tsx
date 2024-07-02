import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './ride-request.reducer';

export const RideRequestDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const rideRequestEntity = useAppSelector(state => state.rideRequest.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="rideRequestDetailsHeading">
          <Translate contentKey="wayShareApp.rideRequest.detail.title">RideRequest</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{rideRequestEntity.id}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="wayShareApp.rideRequest.status">Status</Translate>
            </span>
          </dt>
          <dd>{rideRequestEntity.status}</dd>
          <dt>
            <span id="requestTime">
              <Translate contentKey="wayShareApp.rideRequest.requestTime">Request Time</Translate>
            </span>
          </dt>
          <dd>
            {rideRequestEntity.requestTime ? (
              <TextFormat value={rideRequestEntity.requestTime} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="wayShareApp.rideRequest.ride">Ride</Translate>
          </dt>
          <dd>{rideRequestEntity.ride ? rideRequestEntity.ride.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/ride-request" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/ride-request/${rideRequestEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default RideRequestDetail;
