package com.voituri.ridesharing.domain;

import static com.voituri.ridesharing.domain.MemberTestSamples.*;
import static com.voituri.ridesharing.domain.NotificationTestSamples.*;
import static com.voituri.ridesharing.domain.ProfileTestSamples.*;
import static com.voituri.ridesharing.domain.RatingTestSamples.*;
import static com.voituri.ridesharing.domain.RideTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.voituri.ridesharing.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class MemberTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Member.class);
        Member member1 = getMemberSample1();
        Member member2 = new Member();
        assertThat(member1).isNotEqualTo(member2);

        member2.setId(member1.getId());
        assertThat(member1).isEqualTo(member2);

        member2 = getMemberSample2();
        assertThat(member1).isNotEqualTo(member2);
    }

    @Test
    void profileTest() {
        Member member = getMemberRandomSampleGenerator();
        Profile profileBack = getProfileRandomSampleGenerator();

        member.setProfile(profileBack);
        assertThat(member.getProfile()).isEqualTo(profileBack);

        member.profile(null);
        assertThat(member.getProfile()).isNull();
    }

    @Test
    void ridesTest() {
        Member member = getMemberRandomSampleGenerator();
        Ride rideBack = getRideRandomSampleGenerator();

        member.addRides(rideBack);
        assertThat(member.getRides()).containsOnly(rideBack);
        assertThat(rideBack.getMember()).isEqualTo(member);

        member.removeRides(rideBack);
        assertThat(member.getRides()).doesNotContain(rideBack);
        assertThat(rideBack.getMember()).isNull();

        member.rides(new HashSet<>(Set.of(rideBack)));
        assertThat(member.getRides()).containsOnly(rideBack);
        assertThat(rideBack.getMember()).isEqualTo(member);

        member.setRides(new HashSet<>());
        assertThat(member.getRides()).doesNotContain(rideBack);
        assertThat(rideBack.getMember()).isNull();
    }

    @Test
    void notificationsTest() {
        Member member = getMemberRandomSampleGenerator();
        Notification notificationBack = getNotificationRandomSampleGenerator();

        member.addNotifications(notificationBack);
        assertThat(member.getNotifications()).containsOnly(notificationBack);
        assertThat(notificationBack.getMember()).isEqualTo(member);

        member.removeNotifications(notificationBack);
        assertThat(member.getNotifications()).doesNotContain(notificationBack);
        assertThat(notificationBack.getMember()).isNull();

        member.notifications(new HashSet<>(Set.of(notificationBack)));
        assertThat(member.getNotifications()).containsOnly(notificationBack);
        assertThat(notificationBack.getMember()).isEqualTo(member);

        member.setNotifications(new HashSet<>());
        assertThat(member.getNotifications()).doesNotContain(notificationBack);
        assertThat(notificationBack.getMember()).isNull();
    }

    @Test
    void ratingsGivenTest() {
        Member member = getMemberRandomSampleGenerator();
        Rating ratingBack = getRatingRandomSampleGenerator();

        member.addRatingsGiven(ratingBack);
        assertThat(member.getRatingsGivens()).containsOnly(ratingBack);
        assertThat(ratingBack.getGiver()).isEqualTo(member);

        member.removeRatingsGiven(ratingBack);
        assertThat(member.getRatingsGivens()).doesNotContain(ratingBack);
        assertThat(ratingBack.getGiver()).isNull();

        member.ratingsGivens(new HashSet<>(Set.of(ratingBack)));
        assertThat(member.getRatingsGivens()).containsOnly(ratingBack);
        assertThat(ratingBack.getGiver()).isEqualTo(member);

        member.setRatingsGivens(new HashSet<>());
        assertThat(member.getRatingsGivens()).doesNotContain(ratingBack);
        assertThat(ratingBack.getGiver()).isNull();
    }

    @Test
    void ratingsReceivedTest() {
        Member member = getMemberRandomSampleGenerator();
        Rating ratingBack = getRatingRandomSampleGenerator();

        member.addRatingsReceived(ratingBack);
        assertThat(member.getRatingsReceiveds()).containsOnly(ratingBack);
        assertThat(ratingBack.getReceiver()).isEqualTo(member);

        member.removeRatingsReceived(ratingBack);
        assertThat(member.getRatingsReceiveds()).doesNotContain(ratingBack);
        assertThat(ratingBack.getReceiver()).isNull();

        member.ratingsReceiveds(new HashSet<>(Set.of(ratingBack)));
        assertThat(member.getRatingsReceiveds()).containsOnly(ratingBack);
        assertThat(ratingBack.getReceiver()).isEqualTo(member);

        member.setRatingsReceiveds(new HashSet<>());
        assertThat(member.getRatingsReceiveds()).doesNotContain(ratingBack);
        assertThat(ratingBack.getReceiver()).isNull();
    }
}
