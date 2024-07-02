package com.voituri.ridesharing.service.mapper;

import static com.voituri.ridesharing.domain.MessageAsserts.*;
import static com.voituri.ridesharing.domain.MessageTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MessageMapperTest {

    private MessageMapper messageMapper;

    @BeforeEach
    void setUp() {
        messageMapper = new MessageMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMessageSample1();
        var actual = messageMapper.toEntity(messageMapper.toDto(expected));
        assertMessageAllPropertiesEquals(expected, actual);
    }
}
