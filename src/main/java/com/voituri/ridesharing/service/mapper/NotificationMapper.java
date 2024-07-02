package com.voituri.ridesharing.service.mapper;

import com.voituri.ridesharing.domain.Member;
import com.voituri.ridesharing.domain.Notification;
import com.voituri.ridesharing.service.dto.MemberDTO;
import com.voituri.ridesharing.service.dto.NotificationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Notification} and its DTO {@link NotificationDTO}.
 */
@Mapper(componentModel = "spring")
public interface NotificationMapper extends EntityMapper<NotificationDTO, Notification> {
    @Mapping(target = "member", source = "member", qualifiedByName = "memberId")
    NotificationDTO toDto(Notification s);

    @Named("memberId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MemberDTO toDtoMemberId(Member member);
}
