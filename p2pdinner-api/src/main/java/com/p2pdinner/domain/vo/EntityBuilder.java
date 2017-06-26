package com.p2pdinner.domain.vo;

public interface EntityBuilder<T, R> {
	T build(R r) throws Exception;
}
