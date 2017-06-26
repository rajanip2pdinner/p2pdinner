package com.p2pdinner.repositories;

import com.p2pdinner.domain.Tier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TierRepository extends JpaRepository<Tier, Integer> {
	Tier findByTierName(String tierName);
}
