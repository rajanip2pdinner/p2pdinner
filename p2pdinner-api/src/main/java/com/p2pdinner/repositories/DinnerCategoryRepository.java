package com.p2pdinner.repositories;

import com.p2pdinner.domain.DinnerCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DinnerCategoryRepository extends JpaRepository<DinnerCategory, Integer> {
	DinnerCategory findOneByName(String name);
}
