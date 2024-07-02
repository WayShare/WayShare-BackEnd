import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './member.reducer';

export const MemberDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const memberEntity = useAppSelector(state => state.member.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="memberDetailsHeading">
          <Translate contentKey="wayShareApp.member.detail.title">Member</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{memberEntity.id}</dd>
          <dt>
            <span id="login">
              <Translate contentKey="wayShareApp.member.login">Login</Translate>
            </span>
          </dt>
          <dd>{memberEntity.login}</dd>
          <dt>
            <span id="passwordHash">
              <Translate contentKey="wayShareApp.member.passwordHash">Password Hash</Translate>
            </span>
          </dt>
          <dd>{memberEntity.passwordHash}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="wayShareApp.member.email">Email</Translate>
            </span>
          </dt>
          <dd>{memberEntity.email}</dd>
          <dt>
            <span id="activated">
              <Translate contentKey="wayShareApp.member.activated">Activated</Translate>
            </span>
          </dt>
          <dd>{memberEntity.activated ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="wayShareApp.member.profile">Profile</Translate>
          </dt>
          <dd>{memberEntity.profile ? memberEntity.profile.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/member" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/member/${memberEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MemberDetail;
