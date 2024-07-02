import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './profile.reducer';

export const ProfileDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const profileEntity = useAppSelector(state => state.profile.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="profileDetailsHeading">
          <Translate contentKey="wayShareApp.profile.detail.title">Profile</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{profileEntity.id}</dd>
          <dt>
            <span id="firstName">
              <Translate contentKey="wayShareApp.profile.firstName">First Name</Translate>
            </span>
          </dt>
          <dd>{profileEntity.firstName}</dd>
          <dt>
            <span id="lastName">
              <Translate contentKey="wayShareApp.profile.lastName">Last Name</Translate>
            </span>
          </dt>
          <dd>{profileEntity.lastName}</dd>
          <dt>
            <span id="photo">
              <Translate contentKey="wayShareApp.profile.photo">Photo</Translate>
            </span>
          </dt>
          <dd>{profileEntity.photo}</dd>
          <dt>
            <span id="contactDetails">
              <Translate contentKey="wayShareApp.profile.contactDetails">Contact Details</Translate>
            </span>
          </dt>
          <dd>{profileEntity.contactDetails}</dd>
        </dl>
        <Button tag={Link} to="/profile" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/profile/${profileEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProfileDetail;
