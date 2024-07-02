package com.voituri.ridesharing.service.mapper;

import com.voituri.ridesharing.domain.Profile;
import com.voituri.ridesharing.service.dto.ProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Profile} and its DTO {@link ProfileDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProfileMapper extends EntityMapper<ProfileDTO, Profile> {}
