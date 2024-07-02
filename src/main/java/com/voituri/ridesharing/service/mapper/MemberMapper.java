package com.voituri.ridesharing.service.mapper;

import com.voituri.ridesharing.domain.Member;
import com.voituri.ridesharing.domain.Profile;
import com.voituri.ridesharing.service.dto.MemberDTO;
import com.voituri.ridesharing.service.dto.ProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Member} and its DTO {@link MemberDTO}.
 */
@Mapper(componentModel = "spring")
public interface MemberMapper extends EntityMapper<MemberDTO, Member> {
    @Mapping(target = "profile", source = "profile", qualifiedByName = "profileId")
    MemberDTO toDto(Member s);

    @Named("profileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProfileDTO toDtoProfileId(Profile profile);
}
