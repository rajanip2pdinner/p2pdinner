package com.p2p.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.p2p.domain.Device;

public interface DeviceRepository extends JpaRepository<Device, Integer> {

}
