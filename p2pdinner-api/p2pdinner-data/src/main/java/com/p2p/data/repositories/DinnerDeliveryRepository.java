package com.p2p.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.p2p.domain.DinnerDelivery;

public interface DinnerDeliveryRepository extends JpaRepository<DinnerDelivery, Integer> {
	DinnerDelivery findOneByName(String name);
}
