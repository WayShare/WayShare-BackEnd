package com.voituri.ridesharing.service.mapper;

import static com.voituri.ridesharing.domain.RideAsserts.*;
import static com.voituri.ridesharing.domain.RideTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RideMapperTest {

    private RideMapper rideMapper;

    @BeforeEach
    void setUp() {
        rideMapper = new RideMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getRideSample1();
        var actual = rideMapper.toEntity(rideMapper.toDto(expected));
        assertRideAllPropertiesEquals(expected, actual);
    }
}
