import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/member">
        <Translate contentKey="global.menu.entities.member" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/profile">
        <Translate contentKey="global.menu.entities.profile" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/ride">
        <Translate contentKey="global.menu.entities.ride" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/ride-request">
        <Translate contentKey="global.menu.entities.rideRequest" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/notification">
        <Translate contentKey="global.menu.entities.notification" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/message">
        <Translate contentKey="global.menu.entities.message" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/rating">
        <Translate contentKey="global.menu.entities.rating" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
