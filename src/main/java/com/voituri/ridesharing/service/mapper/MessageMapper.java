package com.voituri.ridesharing.service.mapper;

import com.voituri.ridesharing.domain.Message;
import com.voituri.ridesharing.domain.Ride;
import com.voituri.ridesharing.service.dto.MessageDTO;
import com.voituri.ridesharing.service.dto.RideDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Message} and its DTO {@link MessageDTO}.
 */
@Mapper(componentModel = "spring")
public interface MessageMapper extends EntityMapper<MessageDTO, Message> {
    @Mapping(target = "ride", source = "ride", qualifiedByName = "rideId")
    MessageDTO toDto(Message s);

    @Named("rideId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RideDTO toDtoRideId(Ride ride);
}
