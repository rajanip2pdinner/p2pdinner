package com.p2p.data.config;

public enum Constants {
	
	P2P_JDBC_URL("jdbcUrl"), 
	P2P_DRIVER_CLASSNAME("driverClassName"),
	P2P_USER("user"),
	P2P_PASSWORD("password"),
	P2P_MIN_POOL_SIZE("minPoolSize"),
	P2P_MAX_POOL_SIZE("maxPoolSize"),
	P2P_ACQUIRE_INCREMENT("acquireIncrement"),
	
	STRIPE_AMOUNT("amount"),
	STRIPE_CURRENCY("currency"),
	STRIPE_CARD("card"),
	STRIPE_DESCRIPTION("descriptoin");
	
	private String name;
	
	Constants(String name) {
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
}
