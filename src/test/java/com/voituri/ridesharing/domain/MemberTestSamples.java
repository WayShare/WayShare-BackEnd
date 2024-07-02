package com.voituri.ridesharing.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MemberTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Member getMemberSample1() {
        return new Member().id(1L).login("login1").passwordHash("passwordHash1").email("email1");
    }

    public static Member getMemberSample2() {
        return new Member().id(2L).login("login2").passwordHash("passwordHash2").email("email2");
    }

    public static Member getMemberRandomSampleGenerator() {
        return new Member()
            .id(longCount.incrementAndGet())
            .login(UUID.randomUUID().toString())
            .passwordHash(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString());
    }
}
