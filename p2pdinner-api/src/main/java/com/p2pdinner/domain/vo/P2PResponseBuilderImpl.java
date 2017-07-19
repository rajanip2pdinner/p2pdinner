package com.p2pdinner.domain.vo;

import org.apache.commons.collections.Transformer;

public class P2PResponseBuilderImpl<S,D> implements P2PResponseBuilder<S, D> {
	@SuppressWarnings("unchecked")
	public D buildResponse(S entity, D entityVO, Transformer transformer) throws Exception {
		if ( entity == null ) {
			return null;
		}
		Object object = transformer.transform(entity);
		entityVO = (D) object;
		return entityVO;
	}

}
