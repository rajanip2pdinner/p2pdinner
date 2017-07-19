package com.p2pdinner.service;

import com.p2pdinner.repositories.TierRepository;
import com.p2pdinner.domain.Tier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

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
