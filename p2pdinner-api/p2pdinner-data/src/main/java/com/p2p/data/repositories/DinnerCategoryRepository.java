package com.p2p.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.p2p.domain.DinnerCategory;

public interface DinnerCategoryRepository extends JpaRepository<DinnerCategory, Integer> {
	DinnerCategory findOneByName(String name);
}
