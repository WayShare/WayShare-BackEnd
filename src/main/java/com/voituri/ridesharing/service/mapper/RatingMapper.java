package com.voituri.ridesharing.service.mapper;

import com.voituri.ridesharing.domain.Member;
import com.voituri.ridesharing.domain.Rating;
import com.voituri.ridesharing.service.dto.MemberDTO;
import com.voituri.ridesharing.service.dto.RatingDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Rating} and its DTO {@link RatingDTO}.
 */
@Mapper(componentModel = "spring")
public interface RatingMapper extends EntityMapper<RatingDTO, Rating> {
    @Mapping(target = "giver", source = "giver", qualifiedByName = "memberId")
    @Mapping(target = "receiver", source = "receiver", qualifiedByName = "memberId")
    RatingDTO toDto(Rating s);

    @Named("memberId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MemberDTO toDtoMemberId(Member member);
}
