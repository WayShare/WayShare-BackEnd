package com.voituri.ridesharing.repository;

import com.voituri.ridesharing.domain.Rating;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Rating entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {}
