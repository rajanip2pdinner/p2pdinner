package com.p2p.poc;

import java.beans.PropertyDescriptor;

import org.junit.Test;
import org.springframework.beans.BeanUtils;

public class BeanUtilsTest {

	@Test
	public void testPropertyDescriptors() throws Exception {
		HelloWorld h = new HelloWorld();
		h.setMsg("Hello World .....Read with reflection");
		PropertyDescriptor p = BeanUtils.getPropertyDescriptor(HelloWorld.class, "msg");
		System.out.println(p.getReadMethod().invoke(h));
	}
}
