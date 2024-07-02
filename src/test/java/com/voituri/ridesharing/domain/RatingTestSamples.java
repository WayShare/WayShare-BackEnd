package com.voituri.ridesharing.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class RatingTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Rating getRatingSample1() {
        return new Rating().id(1L).score(1).feedback("feedback1");
    }

    public static Rating getRatingSample2() {
        return new Rating().id(2L).score(2).feedback("feedback2");
    }

    public static Rating getRatingRandomSampleGenerator() {
        return new Rating().id(longCount.incrementAndGet()).score(intCount.incrementAndGet()).feedback(UUID.randomUUID().toString());
    }
}
