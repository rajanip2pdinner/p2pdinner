package com.p2pdinner.repositories;

import com.p2pdinner.domain.KeyValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KeyValueRepository extends JpaRepository<KeyValue, Integer> {
	KeyValue findOneByKey(String key);
}
