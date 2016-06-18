package com.p2p.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.p2p.domain.State;

public interface StateRepository extends JpaRepository<State, Integer> {

}
