package com.voituri.ridesharing.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RideTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Ride getRideSample1() {
        return new Ride().id(1L).startLocation("startLocation1").endLocation("endLocation1");
    }

    public static Ride getRideSample2() {
        return new Ride().id(2L).startLocation("startLocation2").endLocation("endLocation2");
    }

    public static Ride getRideRandomSampleGenerator() {
        return new Ride()
            .id(longCount.incrementAndGet())
            .startLocation(UUID.randomUUID().toString())
            .endLocation(UUID.randomUUID().toString());
    }
}
