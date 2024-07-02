package com.voituri.ridesharing.service.mapper;

import static com.voituri.ridesharing.domain.RatingAsserts.*;
import static com.voituri.ridesharing.domain.RatingTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RatingMapperTest {

    private RatingMapper ratingMapper;

    @BeforeEach
    void setUp() {
        ratingMapper = new RatingMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getRatingSample1();
        var actual = ratingMapper.toEntity(ratingMapper.toDto(expected));
        assertRatingAllPropertiesEquals(expected, actual);
    }
}
