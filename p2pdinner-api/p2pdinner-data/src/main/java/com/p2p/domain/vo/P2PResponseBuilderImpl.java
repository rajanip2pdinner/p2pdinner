package com.p2p.domain.vo;

import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;

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
