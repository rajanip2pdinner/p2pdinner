package com.p2p.data.service;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.p2p.data.repositories.TierRepository;
import com.p2p.domain.Tier;

@Component
public class TierDataService {
	private static final Logger LOGGER = LoggerFactory.getLogger(TierDataService.class);
	
	@Autowired
	private TierRepository tierRepository;
	
	public Collection<Tier> tiers(){
		return tierRepository.findAll();
	}
	
	public Tier tierByName(String tierName) {
		return tierRepository.findByTierName(tierName);
	}
}
