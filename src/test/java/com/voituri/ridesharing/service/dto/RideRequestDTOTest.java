package com.voituri.ridesharing.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.voituri.ridesharing.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RideRequestDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RideRequestDTO.class);
        RideRequestDTO rideRequestDTO1 = new RideRequestDTO();
        rideRequestDTO1.setId(1L);
        RideRequestDTO rideRequestDTO2 = new RideRequestDTO();
        assertThat(rideRequestDTO1).isNotEqualTo(rideRequestDTO2);
        rideRequestDTO2.setId(rideRequestDTO1.getId());
        assertThat(rideRequestDTO1).isEqualTo(rideRequestDTO2);
        rideRequestDTO2.setId(2L);
        assertThat(rideRequestDTO1).isNotEqualTo(rideRequestDTO2);
        rideRequestDTO1.setId(null);
        assertThat(rideRequestDTO1).isNotEqualTo(rideRequestDTO2);
    }
}
