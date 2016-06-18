package com.p2p.common.s3;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.p2p.common.config.P2PCommonContextConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = P2PCommonContextConfig.class)
public class StorageOperationsTest {
	@Autowired
	private StorageOperations storageOperations;
	
	@Test
	@Ignore
	public void testUpload() throws Exception {
		Resource resource = new ClassPathResource("/p2pdinner.yaml");
		storageOperations.uploadObject(resource.getFile().getAbsolutePath());
	}
}
