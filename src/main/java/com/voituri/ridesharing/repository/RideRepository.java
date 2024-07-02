package com.voituri.ridesharing.repository;

import com.voituri.ridesharing.domain.Ride;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Ride entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {}
