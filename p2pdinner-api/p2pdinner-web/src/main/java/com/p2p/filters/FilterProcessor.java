package com.p2p.filters;

import java.util.Collection;

public interface FilterProcessor<T> {
	Collection<T> applyFilters(String qry, Collection<T> source)  throws Exception;
}
