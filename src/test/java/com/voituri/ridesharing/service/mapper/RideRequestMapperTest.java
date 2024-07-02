package com.voituri.ridesharing.service.mapper;

import static com.voituri.ridesharing.domain.RideRequestAsserts.*;
import static com.voituri.ridesharing.domain.RideRequestTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RideRequestMapperTest {

    private RideRequestMapper rideRequestMapper;

    @BeforeEach
    void setUp() {
        rideRequestMapper = new RideRequestMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getRideRequestSample1();
        var actual = rideRequestMapper.toEntity(rideRequestMapper.toDto(expected));
        assertRideRequestAllPropertiesEquals(expected, actual);
    }
}
