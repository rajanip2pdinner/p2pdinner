package com.p2pdinner.web.filters;

import com.p2pdinner.domain.DinnerListing;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.PredicateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;
import java.util.*;

public class DinnerListingFilterProcessor implements FilterProcessor<DinnerListing>, InitializingBean {
	private Map<String,Class<?>> filters;
	
	@SuppressWarnings("unchecked")
	@Override
	public Collection<DinnerListing> applyFilters(String qry, Collection<DinnerListing> source) throws Exception {
		List<Predicate> allPredicates = new ArrayList<Predicate>();
		if (StringUtils.isBlank(qry)) {
			return Collections.emptyList();
		}
		String[] searchQuery = qry.split("\\|");
		for(String q : searchQuery) {
			String[] filter = q.split("::");
			if (filter != null && filter.length == 0) {
				continue;
			}
			if (filters.containsKey(filter[0])) {
				if ( filter != null && filter.length > 1) {
					FilterMapper filterMapper = filters.get(filter[0]).getAnnotation(FilterMapper.class);
					if ( filterMapper.requireMultipleValues()) {
						Predicate p = (Predicate) filters.get(filter[0]).getDeclaredConstructor().newInstance();
						for(Method method : p.getClass().getDeclaredMethods()) {
							if (method.isAnnotationPresent(InitMethod.class)) {
								String[] tokens = StringUtils.split(filter[1], ",");
								method.invoke(p, new Object[] {tokens});
							}
						}
						//Predicate p = (Predicate) filters.get(filter[0]).getDeclaredConstructor(String[].class).newInstance((String[])StringUtils.split(filter[1], ","));
						allPredicates.add(p);
					} else {
						Predicate p = (Predicate) filters.get(filter[0]).getDeclaredConstructor(String.class).newInstance(filter[1]);
						allPredicates.add(p);
					}

				}
			}
		}
		return CollectionUtils.select(source, PredicateUtils.allPredicate(allPredicates));
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		filters = new HashMap<>();
		ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
		scanner.addIncludeFilter(new AnnotationTypeFilter(FilterMapper.class));
		for(BeanDefinition defn : scanner.findCandidateComponents("com.p2p")){
			Class<?> clazz = ClassUtils.resolveClassName(defn.getBeanClassName(), ClassUtils.getDefaultClassLoader());
			FilterMapper filterMapper = clazz.getAnnotation(FilterMapper.class);
			System.out.println(filterMapper + "----" + defn.getBeanClassName());
			filters.put(filterMapper.keyword(), clazz);
		}
	}

}
