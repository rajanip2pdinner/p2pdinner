package com.p2p.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.p2p.domain.Tier;

@Repository
public interface TierRepository extends JpaRepository<Tier, Integer> {
	Tier findByTierName(String tierName);
}
