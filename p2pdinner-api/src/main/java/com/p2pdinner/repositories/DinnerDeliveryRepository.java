package com.p2pdinner.repositories;

import com.p2pdinner.domain.DinnerDelivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface DinnerDeliveryRepository extends JpaRepository<DinnerDelivery, Integer> {
	DinnerDelivery findOneByName(String name);
	Collection<DinnerDelivery> findByIsActiveTrue();
}
