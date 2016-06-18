package com.p2p.domain.vo;

public interface EntityBuilder<T, R> {
	T build(R r) throws Exception;
}
