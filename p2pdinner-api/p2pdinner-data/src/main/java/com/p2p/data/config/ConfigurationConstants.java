package com.p2p.data.config;

public enum ConfigurationConstants {
	P2PDINNER_JDBC_URL("p2dinner.ds.jdbcUrl"),
	P2PDINNER_DS_USER("p2dinner.ds.user"),
	P2PDINNER_DS_PASSWORD("p2dinner.ds.password"),
	P2PDINNER_DS_MAXSTATEMENTS("p2dinner.ds.maxStatements"),
	P2PDINNER_DS_MINPOOLSIZE("p2dinner.ds.minPoolSize"),
	P2PDINNER_DS_MAXPOOLSIZE("p2dinner.ds.maxPoolSize"),
	P2PDINNER_DS_ACQUIREINCREMENTS("p2dinner.ds.acquireIncrements"),
	P2PDINNER_DS_DRIVERCLASS("p2dinner.ds.driverClass");
	
	
	private String name;
	
	ConfigurationConstants(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
}
