package com.p2p.rest.client;

import com.p2p.data.config.P2PTestContextConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 * Created by rajaniy on 1/6/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = P2PTestContextConfiguration.class, loader = AnnotationConfigContextLoader.class)
public class GCMSendTest {

    @Autowired
    private Environment env;

    @Autowired
    private GCMSender gcmSender;

    @Test
    public void testNotification() throws Exception {
        gcmSender.sendNotification("eC4IEnjyZ_Q:APA91bHBkD8kwihGJbNafdW0Oexd7OqTDLv2BR3ao3mtPiJ3XPqa4BihicGZcNDiY3rvBYd_SG07Cpmk90IvrlKMKk5AYc-Y8Z6a43WNw0VrkD0a02j-BRXVZ3nEcgJCU43DF2P18suP", "1234456", 5.95);
    }
}
