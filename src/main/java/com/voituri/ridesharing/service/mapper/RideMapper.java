package com.voituri.ridesharing.service.mapper;

import com.voituri.ridesharing.domain.Member;
import com.voituri.ridesharing.domain.Ride;
import com.voituri.ridesharing.service.dto.MemberDTO;
import com.voituri.ridesharing.service.dto.RideDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Ride} and its DTO {@link RideDTO}.
 */
@Mapper(componentModel = "spring")
public interface RideMapper extends EntityMapper<RideDTO, Ride> {
    @Mapping(target = "member", source = "member", qualifiedByName = "memberId")
    RideDTO toDto(Ride s);

    @Named("memberId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MemberDTO toDtoMemberId(Member member);
}
