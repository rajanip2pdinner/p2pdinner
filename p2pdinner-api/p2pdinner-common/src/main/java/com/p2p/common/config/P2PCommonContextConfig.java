package com.p2p.common.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.ServiceLocatorFactoryBean;
import org.springframework.beans.factory.serviceloader.ServiceFactoryBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.UrlTemplateResolver;

import com.p2p.common.notifications.PushNotification;
import com.p2p.common.notifications.PushNotificationFactory;

@Configuration
@ComponentScan(basePackages = {"com.p2p.common"} )
public class P2PCommonContextConfig {
	
	private static final String THYME_TEMPATE_URI_PREFIX = "https://s3-us-west-1.amazonaws.com/p2pdinner-assets/thyme";
	
	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:locale/messages");
		return messageSource;
	}
	
	@Bean(name = "emailTemplateResolver")
	public ITemplateResolver emailTemplateResolver() {
		UrlTemplateResolver templateResolver = new UrlTemplateResolver();
		templateResolver.setPrefix(THYME_TEMPATE_URI_PREFIX);
		templateResolver.setCharacterEncoding("UTF-8");
		templateResolver.setTemplateMode("HTML5");
		templateResolver.setOrder(1);
		return templateResolver;
	}
	
	@Bean
	public SpringTemplateEngine templateEngine() {
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		Set<ITemplateResolver> templateResolvers = new HashSet<ITemplateResolver>();
		templateResolvers.add(emailTemplateResolver());
		templateEngine.setTemplateResolvers(templateResolvers);
		return templateEngine;
	}

	@Bean
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setRequestFactory(new SimpleClientHttpRequestFactory());
		return restTemplate;
	}
	
	@Bean(name = "pushNotificationFactoryBean")
	public ServiceLocatorFactoryBean serviceFactory() {
		ServiceLocatorFactoryBean serviceLocatorFactoryBean = new ServiceLocatorFactoryBean();
		serviceLocatorFactoryBean.setServiceLocatorInterface(PushNotificationFactory.class);
		return serviceLocatorFactoryBean;
	}

	@Bean(name = "pushNotificationFactory")
	public PushNotificationFactory pushNotificationFactory(){
		return (PushNotificationFactory)serviceFactory().getObject();
	}
	
}
