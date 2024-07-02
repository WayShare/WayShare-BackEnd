package com.voituri.ridesharing.repository;

import com.voituri.ridesharing.domain.Member;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Member entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {}
