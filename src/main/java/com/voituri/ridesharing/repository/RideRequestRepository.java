package com.voituri.ridesharing.repository;

import com.voituri.ridesharing.domain.RideRequest;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the RideRequest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RideRequestRepository extends JpaRepository<RideRequest, Long> {}
