package com.voituri.ridesharing.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RideRequestTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static RideRequest getRideRequestSample1() {
        return new RideRequest().id(1L).status("status1");
    }

    public static RideRequest getRideRequestSample2() {
        return new RideRequest().id(2L).status("status2");
    }

    public static RideRequest getRideRequestRandomSampleGenerator() {
        return new RideRequest().id(longCount.incrementAndGet()).status(UUID.randomUUID().toString());
    }
}
