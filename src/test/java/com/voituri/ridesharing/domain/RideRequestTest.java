package com.voituri.ridesharing.domain;

import static com.voituri.ridesharing.domain.RideRequestTestSamples.*;
import static com.voituri.ridesharing.domain.RideTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.voituri.ridesharing.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RideRequestTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RideRequest.class);
        RideRequest rideRequest1 = getRideRequestSample1();
        RideRequest rideRequest2 = new RideRequest();
        assertThat(rideRequest1).isNotEqualTo(rideRequest2);

        rideRequest2.setId(rideRequest1.getId());
        assertThat(rideRequest1).isEqualTo(rideRequest2);

        rideRequest2 = getRideRequestSample2();
        assertThat(rideRequest1).isNotEqualTo(rideRequest2);
    }

    @Test
    void rideTest() {
        RideRequest rideRequest = getRideRequestRandomSampleGenerator();
        Ride rideBack = getRideRandomSampleGenerator();

        rideRequest.setRide(rideBack);
        assertThat(rideRequest.getRide()).isEqualTo(rideBack);

        rideRequest.ride(null);
        assertThat(rideRequest.getRide()).isNull();
    }
}
