import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './ride.reducer';

export const Ride = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const rideList = useAppSelector(state => state.ride.entities);
  const loading = useAppSelector(state => state.ride.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="ride-heading" data-cy="RideHeading">
        <Translate contentKey="wayShareApp.ride.home.title">Rides</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="wayShareApp.ride.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/ride/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="wayShareApp.ride.home.createLabel">Create new Ride</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {rideList && rideList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="wayShareApp.ride.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('startLocation')}>
                  <Translate contentKey="wayShareApp.ride.startLocation">Start Location</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('startLocation')} />
                </th>
                <th className="hand" onClick={sort('endLocation')}>
                  <Translate contentKey="wayShareApp.ride.endLocation">End Location</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('endLocation')} />
                </th>
                <th className="hand" onClick={sort('startTime')}>
                  <Translate contentKey="wayShareApp.ride.startTime">Start Time</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('startTime')} />
                </th>
                <th className="hand" onClick={sort('endTime')}>
                  <Translate contentKey="wayShareApp.ride.endTime">End Time</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('endTime')} />
                </th>
                <th className="hand" onClick={sort('recurring')}>
                  <Translate contentKey="wayShareApp.ride.recurring">Recurring</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('recurring')} />
                </th>
                <th>
                  <Translate contentKey="wayShareApp.ride.member">Member</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {rideList.map((ride, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/ride/${ride.id}`} color="link" size="sm">
                      {ride.id}
                    </Button>
                  </td>
                  <td>{ride.startLocation}</td>
                  <td>{ride.endLocation}</td>
                  <td>{ride.startTime ? <TextFormat type="date" value={ride.startTime} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{ride.endTime ? <TextFormat type="date" value={ride.endTime} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{ride.recurring ? 'true' : 'false'}</td>
                  <td>{ride.member ? <Link to={`/member/${ride.member.id}`}>{ride.member.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/ride/${ride.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/ride/${ride.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/ride/${ride.id}/delete`)}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="wayShareApp.ride.home.notFound">No Rides found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Ride;
