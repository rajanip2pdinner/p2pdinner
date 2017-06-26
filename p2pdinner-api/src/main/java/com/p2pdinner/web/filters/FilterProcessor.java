package com.p2pdinner.web.filters;

import java.util.Collection;

public interface FilterProcessor<T> {
	Collection<T> applyFilters(String qry, Collection<T> source)  throws Exception;
}
