package com.voituri.ridesharing.service.mapper;

import com.voituri.ridesharing.domain.Ride;
import com.voituri.ridesharing.domain.RideRequest;
import com.voituri.ridesharing.service.dto.RideDTO;
import com.voituri.ridesharing.service.dto.RideRequestDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RideRequest} and its DTO {@link RideRequestDTO}.
 */
@Mapper(componentModel = "spring")
public interface RideRequestMapper extends EntityMapper<RideRequestDTO, RideRequest> {
    @Mapping(target = "ride", source = "ride", qualifiedByName = "rideId")
    RideRequestDTO toDto(RideRequest s);

    @Named("rideId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RideDTO toDtoRideId(Ride ride);
}
