package com.p2p.messaging.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = { "com.p2p"})
@ComponentScan(basePackages = { "com.p2p"})
@EnableScheduling
@EnableAsync
@PropertySource("classpath:/messaging.properties")
public class P2PMessagingServerContext extends AbstractP2PMessagingContext {

}
