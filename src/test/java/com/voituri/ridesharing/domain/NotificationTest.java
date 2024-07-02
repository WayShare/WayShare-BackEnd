package com.voituri.ridesharing.domain;

import static com.voituri.ridesharing.domain.MemberTestSamples.*;
import static com.voituri.ridesharing.domain.NotificationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.voituri.ridesharing.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NotificationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Notification.class);
        Notification notification1 = getNotificationSample1();
        Notification notification2 = new Notification();
        assertThat(notification1).isNotEqualTo(notification2);

        notification2.setId(notification1.getId());
        assertThat(notification1).isEqualTo(notification2);

        notification2 = getNotificationSample2();
        assertThat(notification1).isNotEqualTo(notification2);
    }

    @Test
    void memberTest() {
        Notification notification = getNotificationRandomSampleGenerator();
        Member memberBack = getMemberRandomSampleGenerator();

        notification.setMember(memberBack);
        assertThat(notification.getMember()).isEqualTo(memberBack);

        notification.member(null);
        assertThat(notification.getMember()).isNull();
    }
}
