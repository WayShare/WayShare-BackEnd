package com.voituri.ridesharing.domain;

import static com.voituri.ridesharing.domain.MemberTestSamples.*;
import static com.voituri.ridesharing.domain.RatingTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.voituri.ridesharing.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RatingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Rating.class);
        Rating rating1 = getRatingSample1();
        Rating rating2 = new Rating();
        assertThat(rating1).isNotEqualTo(rating2);

        rating2.setId(rating1.getId());
        assertThat(rating1).isEqualTo(rating2);

        rating2 = getRatingSample2();
        assertThat(rating1).isNotEqualTo(rating2);
    }

    @Test
    void giverTest() {
        Rating rating = getRatingRandomSampleGenerator();
        Member memberBack = getMemberRandomSampleGenerator();

        rating.setGiver(memberBack);
        assertThat(rating.getGiver()).isEqualTo(memberBack);

        rating.giver(null);
        assertThat(rating.getGiver()).isNull();
    }

    @Test
    void receiverTest() {
        Rating rating = getRatingRandomSampleGenerator();
        Member memberBack = getMemberRandomSampleGenerator();

        rating.setReceiver(memberBack);
        assertThat(rating.getReceiver()).isEqualTo(memberBack);

        rating.receiver(null);
        assertThat(rating.getReceiver()).isNull();
    }
}
