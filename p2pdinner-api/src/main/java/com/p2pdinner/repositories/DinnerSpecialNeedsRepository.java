package com.p2pdinner.repositories;

import com.p2pdinner.domain.DinnerSpecialNeeds;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DinnerSpecialNeedsRepository extends JpaRepository<DinnerSpecialNeeds, Integer> {
	DinnerSpecialNeeds findOneByName(String name);
}
