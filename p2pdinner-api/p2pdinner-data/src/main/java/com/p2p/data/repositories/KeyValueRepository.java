package com.p2p.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.p2p.domain.KeyValue;

@Repository
public interface KeyValueRepository extends JpaRepository<KeyValue, Integer> {
	KeyValue findOneByKey(String key);
}
