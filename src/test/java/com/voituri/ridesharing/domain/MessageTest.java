package com.voituri.ridesharing.domain;

import static com.voituri.ridesharing.domain.MessageTestSamples.*;
import static com.voituri.ridesharing.domain.RideTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.voituri.ridesharing.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MessageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Message.class);
        Message message1 = getMessageSample1();
        Message message2 = new Message();
        assertThat(message1).isNotEqualTo(message2);

        message2.setId(message1.getId());
        assertThat(message1).isEqualTo(message2);

        message2 = getMessageSample2();
        assertThat(message1).isNotEqualTo(message2);
    }

    @Test
    void rideTest() {
        Message message = getMessageRandomSampleGenerator();
        Ride rideBack = getRideRandomSampleGenerator();

        message.setRide(rideBack);
        assertThat(message.getRide()).isEqualTo(rideBack);

        message.ride(null);
        assertThat(message.getRide()).isNull();
    }
}
