package com.p2pdinner.domain.transformers;

import com.p2pdinner.domain.DinnerCategory;
import com.p2pdinner.domain.DinnerDelivery;
import com.p2pdinner.domain.DinnerSpecialNeeds;
import com.p2pdinner.domain.MenuItem;
import com.p2pdinner.domain.vo.MenuItemVO;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;

public class MenuItemTransformer implements Transformer {

	private static final Logger LOGGER = LoggerFactory.getLogger(MenuItemTransformer.class);
	
	public Object transform(Object menuItem) throws RuntimeException {
		if (menuItem != null && menuItem instanceof MenuItem) {
			MenuItem m = (MenuItem) menuItem;
			MenuItemVO menuItemVO = new MenuItemVO();
			BeanUtils.copyProperties(m, menuItemVO);
			menuItemVO.setUserId(m.getUserProfile().getId());
			try {
				copyListAttributes(m, menuItemVO, DinnerCategory.class, "dinnerCategories", "dinnerCategories");
				copyListAttributes(m, menuItemVO, DinnerSpecialNeeds.class, "dinnerSpecialNeeds", "dinnerSpecialNeeds");
				copyListAttributes(m, menuItemVO, DinnerDelivery.class, "dinnerDeliveries", "dinnerDelivery");
			} catch (Exception e) {
				LOGGER.error(e.getMessage(),e);
			}
			return menuItemVO;
		}
		return null;
	}

	@SuppressWarnings({"rawtypes"})
	private void copyListAttributes(MenuItem menuItem, MenuItemVO menuItemVO, Class<?> genericParameterType, String sourceAttrib, String destAttrib) throws Exception {
		Method readMethod = BeanUtils.getPropertyDescriptor(menuItem.getClass(), sourceAttrib).getReadMethod();
		Method writeMethod = BeanUtils.getPropertyDescriptor(menuItemVO.getClass(), destAttrib).getWriteMethod();
		Collection listValues = (Collection) readMethod.invoke(menuItem);
		String[] values = new String[listValues.size()];
		int idx = 0;
		Iterator iter = listValues.iterator();
		while(iter.hasNext()) {
			values[idx++] =(String) BeanUtils.getPropertyDescriptor(genericParameterType, "name").getReadMethod().invoke(iter.next());
		}
		writeMethod.invoke(menuItemVO, new Object[] {StringUtils.join(values, ",")});
	}
	
}
