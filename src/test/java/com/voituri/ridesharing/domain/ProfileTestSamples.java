package com.voituri.ridesharing.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ProfileTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Profile getProfileSample1() {
        return new Profile().id(1L).firstName("firstName1").lastName("lastName1").photo("photo1").contactDetails("contactDetails1");
    }

    public static Profile getProfileSample2() {
        return new Profile().id(2L).firstName("firstName2").lastName("lastName2").photo("photo2").contactDetails("contactDetails2");
    }

    public static Profile getProfileRandomSampleGenerator() {
        return new Profile()
            .id(longCount.incrementAndGet())
            .firstName(UUID.randomUUID().toString())
            .lastName(UUID.randomUUID().toString())
            .photo(UUID.randomUUID().toString())
            .contactDetails(UUID.randomUUID().toString());
    }
}
