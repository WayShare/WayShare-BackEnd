import React, { useState, useEffect } from 'react';
import InfiniteScroll from 'react-infinite-scroll-component';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities, reset } from './notification.reducer';

export const Notification = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );
  const [sorting, setSorting] = useState(false);

  const notificationList = useAppSelector(state => state.notification.entities);
  const loading = useAppSelector(state => state.notification.loading);
  const links = useAppSelector(state => state.notification.links);
  const updateSuccess = useAppSelector(state => state.notification.updateSuccess);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const resetAll = () => {
    dispatch(reset());
    setPaginationState({
      ...paginationState,
      activePage: 1,
    });
    dispatch(getEntities({}));
  };

  useEffect(() => {
    resetAll();
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      resetAll();
    }
  }, [updateSuccess]);

  useEffect(() => {
    getAllEntities();
  }, [paginationState.activePage]);

  const handleLoadMore = () => {
    if ((window as any).pageYOffset > 0) {
      setPaginationState({
        ...paginationState,
        activePage: paginationState.activePage + 1,
      });
    }
  };

  useEffect(() => {
    if (sorting) {
      getAllEntities();
      setSorting(false);
    }
  }, [sorting]);

  const sort = p => () => {
    dispatch(reset());
    setPaginationState({
      ...paginationState,
      activePage: 1,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
    setSorting(true);
  };

  const handleSyncList = () => {
    resetAll();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="notification-heading" data-cy="NotificationHeading">
        <Translate contentKey="wayShareApp.notification.home.title">Notifications</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="wayShareApp.notification.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/notification/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="wayShareApp.notification.home.createLabel">Create new Notification</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        <InfiniteScroll
          dataLength={notificationList ? notificationList.length : 0}
          next={handleLoadMore}
          hasMore={paginationState.activePage - 1 < links.next}
          loader={<div className="loader">Loading ...</div>}
        >
          {notificationList && notificationList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th className="hand" onClick={sort('id')}>
                    <Translate contentKey="wayShareApp.notification.id">ID</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                  </th>
                  <th className="hand" onClick={sort('message')}>
                    <Translate contentKey="wayShareApp.notification.message">Message</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('message')} />
                  </th>
                  <th className="hand" onClick={sort('timestamp')}>
                    <Translate contentKey="wayShareApp.notification.timestamp">Timestamp</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('timestamp')} />
                  </th>
                  <th className="hand" onClick={sort('read')}>
                    <Translate contentKey="wayShareApp.notification.read">Read</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('read')} />
                  </th>
                  <th>
                    <Translate contentKey="wayShareApp.notification.member">Member</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {notificationList.map((notification, i) => (
                  <tr key={`entity-${i}`} data-cy="entityTable">
                    <td>
                      <Button tag={Link} to={`/notification/${notification.id}`} color="link" size="sm">
                        {notification.id}
                      </Button>
                    </td>
                    <td>{notification.message}</td>
                    <td>
                      {notification.timestamp ? <TextFormat type="date" value={notification.timestamp} format={APP_DATE_FORMAT} /> : null}
                    </td>
                    <td>{notification.read ? 'true' : 'false'}</td>
                    <td>{notification.member ? <Link to={`/member/${notification.member.id}`}>{notification.member.id}</Link> : ''}</td>
                    <td className="text-end">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`/notification/${notification.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button
                          tag={Link}
                          to={`/notification/${notification.id}/edit`}
                          color="primary"
                          size="sm"
                          data-cy="entityEditButton"
                        >
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button
                          onClick={() => (window.location.href = `/notification/${notification.id}/delete`)}
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
                <Translate contentKey="wayShareApp.notification.home.notFound">No Notifications found</Translate>
              </div>
            )
          )}
        </InfiniteScroll>
      </div>
    </div>
  );
};

export default Notification;
