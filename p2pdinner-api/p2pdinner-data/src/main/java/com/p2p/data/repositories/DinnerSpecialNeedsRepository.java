package com.p2p.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.p2p.domain.DinnerSpecialNeeds;

public interface DinnerSpecialNeedsRepository extends JpaRepository<DinnerSpecialNeeds, Integer> {
	DinnerSpecialNeeds findOneByName(String name);
}
