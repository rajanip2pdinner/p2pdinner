package com.p2p.messaging.config;

import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.p2p.messaging.services.OrderProcessingExceptionHandler;
import com.p2p.messaging.services.OrderProcessingHandler;
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = { "com.p2p"})
@ComponentScan(basePackages = { "com.p2p"})
@EnableScheduling
@EnableAsync
@PropertySource("classpath:/messaging.properties")
public class P2PMessagingClientContext extends AbstractP2PMessagingContext {
	@Bean(name = "requestMessageListenerAdapter")
	public MessageListenerAdapter requestMessageListenerAdapter() {
		return new MessageListenerAdapter(orderProcessingHandler(),jsonMessageConverter());
	}
	
	@Bean(name = "excepMessageListenerAdapter")
	public MessageListenerAdapter excepMessageListenerAdapter() {
		return new MessageListenerAdapter(orderProcessingExceptionHandler(),jsonMessageConverter());
	}
	
	@Bean(name = "orderProcessingHandler")
	public OrderProcessingHandler orderProcessingHandler(){
		return new OrderProcessingHandler();
	}
	
	@Bean(name = "orderProcessingExceptionHandler")
	public OrderProcessingExceptionHandler orderProcessingExceptionHandler(){
		return new OrderProcessingExceptionHandler();
	}
	
	@Bean(name = "requestQueueListenerContainer")
    public SimpleMessageListenerContainer requestQueueListenerContainer() throws Exception {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory());
        container.setQueueNames(P2PDINNER_REQUEST_QUEUE_NAME);
        container.setMessageListener(requestMessageListenerAdapter());
        return container;
    }
	
	@Bean(name = "excepQueueListenerContainer")
    public SimpleMessageListenerContainer excepQueueListenerContainer() throws Exception {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory());
        container.setQueueNames(P2PDINNER_EXCEP_QUEUE_NAME);
        container.setMessageListener(excepMessageListenerAdapter());
        return container;
    }
	
}
