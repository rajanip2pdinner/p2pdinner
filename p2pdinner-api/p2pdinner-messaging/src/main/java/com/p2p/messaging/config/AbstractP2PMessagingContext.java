package com.p2p.messaging.config;

import java.net.URI;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.StringUtils;

import com.p2p.messaging.services.OrderProcessingServiceImpl;

public abstract class AbstractP2PMessagingContext {
	protected static String P2PDINNER_DATA_EXCHANGE_NAME = "app.p2pdinner.orderdata";
	protected static String P2PDINNER_REQUEST_QUEUE_NAME = "app.p2pdinner.request";
	protected static String P2PDINNER_EXCEP_QUEUE_NAME = "app.p2pdinner.excep";
	public static String P2PDINNER_REQUEST_ROUTING_KEY = P2PDINNER_REQUEST_QUEUE_NAME;
	public static String P2PDINNER_REQUEST_EXCEPTION_ROUTING_KEY = P2PDINNER_EXCEP_QUEUE_NAME;

	@Autowired
	private Environment env;

	@Bean
	public ConnectionFactory connectionFactory() throws Exception {
		CachingConnectionFactory connectionFactory = null;
		String amqpUrl = "";
		if (!StringUtils.isEmpty(System.getenv("CLOUDAMQP_URL"))) {
			amqpUrl = System.getenv("CLOUDAMQP_URL");
			URI uri = new URI(amqpUrl);
			connectionFactory = new CachingConnectionFactory(uri.getHost());
			if (uri.getPort() != -1) {
				connectionFactory.setPort(uri.getPort());
			}
			if (uri.getPath() != null || !uri.getPath().isEmpty()) {
				connectionFactory.setVirtualHost(uri.getPath().substring(1));
			}
			connectionFactory.setUsername(uri.getUserInfo().split(":")[0]);
			connectionFactory.setPassword(uri.getUserInfo().split(":")[1]);
		} else {
			String host = env.getProperty("rabbitmq.server.host");
			String port = env.getProperty("rabbitmq.server.port");
			String username = env.getProperty("rabbitmq.server.username");
			String password = env.getProperty("rabbitmq.server.password");
			connectionFactory = new CachingConnectionFactory(host);
			connectionFactory.setPort(Integer.parseInt(port));
			connectionFactory.setUsername(username);
			connectionFactory.setPassword(password);
		}
		return connectionFactory;
	}

	@Bean
	public RabbitTemplate rabbitTemplate() throws Exception {
		RabbitTemplate template = new RabbitTemplate(connectionFactory());
		template.setMessageConverter(jsonMessageConverter());
		template.setExchange(P2PDINNER_DATA_EXCHANGE_NAME);
		return template;
	}

	@Bean
	public MessageConverter jsonMessageConverter() {
		return new JsonMessageConverter();
	}

	@Bean
	public DirectExchange orderDataExchange() {
		return new DirectExchange(P2PDINNER_DATA_EXCHANGE_NAME);
	}

	/**
	 * @return the admin bean that can declare queues etc.
	 */
	@Bean
	public AmqpAdmin amqpAdmin() throws Exception {
		RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory());
		rabbitAdmin.declareQueue(p2pDinnerOrderQueue());
		rabbitAdmin.declareExchange(orderDataExchange());
		rabbitAdmin.declareBinding(orderDataBinding());
		rabbitAdmin.declareQueue(p2pDinnerExceptionQueue());
		rabbitAdmin.declareBinding(orderExceptionBinding());
		return rabbitAdmin;
	}
	
	@Bean
	public Queue p2pDinnerOrderQueue() {		
		return new Queue(P2PDINNER_REQUEST_QUEUE_NAME);	
	}
	
	@Bean
	public Queue p2pDinnerExceptionQueue() {
		return new Queue(P2PDINNER_EXCEP_QUEUE_NAME);
	}
	
	@Bean(name = "orderDataBinding")
	public Binding orderDataBinding(){
		return BindingBuilder.bind(p2pDinnerOrderQueue()).to(orderDataExchange()).with(P2PDINNER_REQUEST_ROUTING_KEY);
	}
	
	@Bean(name = "orderExceptionBinding")
	public Binding orderExceptionBinding(){
		return BindingBuilder.bind(p2pDinnerExceptionQueue()).to(orderDataExchange()).with(P2PDINNER_REQUEST_EXCEPTION_ROUTING_KEY);
	}
	
	@Bean
	public OrderProcessingServiceImpl orderProcessingService() {
		return new OrderProcessingServiceImpl();
	}

}
