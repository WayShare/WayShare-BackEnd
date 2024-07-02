package com.voituri.ridesharing.domain;

import static com.voituri.ridesharing.domain.MemberTestSamples.*;
import static com.voituri.ridesharing.domain.ProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.voituri.ridesharing.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProfileTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Profile.class);
        Profile profile1 = getProfileSample1();
        Profile profile2 = new Profile();
        assertThat(profile1).isNotEqualTo(profile2);

        profile2.setId(profile1.getId());
        assertThat(profile1).isEqualTo(profile2);

        profile2 = getProfileSample2();
        assertThat(profile1).isNotEqualTo(profile2);
    }

    @Test
    void memberTest() {
        Profile profile = getProfileRandomSampleGenerator();
        Member memberBack = getMemberRandomSampleGenerator();

        profile.setMember(memberBack);
        assertThat(profile.getMember()).isEqualTo(memberBack);
        assertThat(memberBack.getProfile()).isEqualTo(profile);

        profile.member(null);
        assertThat(profile.getMember()).isNull();
        assertThat(memberBack.getProfile()).isNull();
    }
}
