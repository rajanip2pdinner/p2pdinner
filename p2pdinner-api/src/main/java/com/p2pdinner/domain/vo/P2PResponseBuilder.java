package com.p2pdinner.domain.vo;

import org.apache.commons.collections.Transformer;

public interface P2PResponseBuilder<S, D> {
	D buildResponse(S entity, D entityVO, Transformer transformer) throws Exception;
}
