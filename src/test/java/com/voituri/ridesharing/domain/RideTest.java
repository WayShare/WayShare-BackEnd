package com.voituri.ridesharing.domain;

import static com.voituri.ridesharing.domain.MemberTestSamples.*;
import static com.voituri.ridesharing.domain.MessageTestSamples.*;
import static com.voituri.ridesharing.domain.RideRequestTestSamples.*;
import static com.voituri.ridesharing.domain.RideTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.voituri.ridesharing.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class RideTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ride.class);
        Ride ride1 = getRideSample1();
        Ride ride2 = new Ride();
        assertThat(ride1).isNotEqualTo(ride2);

        ride2.setId(ride1.getId());
        assertThat(ride1).isEqualTo(ride2);

        ride2 = getRideSample2();
        assertThat(ride1).isNotEqualTo(ride2);
    }

    @Test
    void requestsTest() {
        Ride ride = getRideRandomSampleGenerator();
        RideRequest rideRequestBack = getRideRequestRandomSampleGenerator();

        ride.addRequests(rideRequestBack);
        assertThat(ride.getRequests()).containsOnly(rideRequestBack);
        assertThat(rideRequestBack.getRide()).isEqualTo(ride);

        ride.removeRequests(rideRequestBack);
        assertThat(ride.getRequests()).doesNotContain(rideRequestBack);
        assertThat(rideRequestBack.getRide()).isNull();

        ride.requests(new HashSet<>(Set.of(rideRequestBack)));
        assertThat(ride.getRequests()).containsOnly(rideRequestBack);
        assertThat(rideRequestBack.getRide()).isEqualTo(ride);

        ride.setRequests(new HashSet<>());
        assertThat(ride.getRequests()).doesNotContain(rideRequestBack);
        assertThat(rideRequestBack.getRide()).isNull();
    }

    @Test
    void messagesTest() {
        Ride ride = getRideRandomSampleGenerator();
        Message messageBack = getMessageRandomSampleGenerator();

        ride.addMessages(messageBack);
        assertThat(ride.getMessages()).containsOnly(messageBack);
        assertThat(messageBack.getRide()).isEqualTo(ride);

        ride.removeMessages(messageBack);
        assertThat(ride.getMessages()).doesNotContain(messageBack);
        assertThat(messageBack.getRide()).isNull();

        ride.messages(new HashSet<>(Set.of(messageBack)));
        assertThat(ride.getMessages()).containsOnly(messageBack);
        assertThat(messageBack.getRide()).isEqualTo(ride);

        ride.setMessages(new HashSet<>());
        assertThat(ride.getMessages()).doesNotContain(messageBack);
        assertThat(messageBack.getRide()).isNull();
    }

    @Test
    void memberTest() {
        Ride ride = getRideRandomSampleGenerator();
        Member memberBack = getMemberRandomSampleGenerator();

        ride.setMember(memberBack);
        assertThat(ride.getMember()).isEqualTo(memberBack);

        ride.member(null);
        assertThat(ride.getMember()).isNull();
    }
}
