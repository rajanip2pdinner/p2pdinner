package com.p2pdinner.common.config;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import com.p2pdinner.client.*;
import com.p2pdinner.common.exceptions.P2PDinnerException;
import com.p2pdinner.common.notifications.PushNotificationFactory;
import com.p2pdinner.common.s3.StorageOperations;
import com.p2pdinner.domain.DinnerCart;
import com.p2pdinner.domain.DinnerListing;
import com.p2pdinner.domain.MenuItem;
import com.p2pdinner.domain.UserProfile;
import com.p2pdinner.domain.vo.*;
import com.p2pdinner.service.PlaceOrderAspect;
import com.p2pdinner.service.StripeDataService;
import com.p2pdinner.tasks.PlacesScheduledTask;
import com.p2pdinner.web.filters.DinnerListingFilterProcessor;
import com.p2pdinner.web.filters.FilterProcessor;
import org.apache.cxf.annotations.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ServiceLocatorFactoryBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.UrlTemplateResolver;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

@Configuration
@ComponentScan(basePackages = {"com.p2pdinner"} )
public class P2PCommonContextConfig {
	
	private static final String THYME_TEMPATE_URI_PREFIX = "https://s3-us-west-1.amazonaws.com/p2pdinner-assets/thyme";

	@Value("${apple.cert}")
	private String appleCertificatePath;
	
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

	@Bean
	public EntityBuilder<MenuItem, MenuItemVO> menuEntityBuilder() {
		EntityBuilder<MenuItem, MenuItemVO> menuEntityBuilder = new MenuItemEntityBuilder();
		return menuEntityBuilder;
	}

	@Bean
	public EntityBuilder<DinnerListing, DinnerListingVO> dinnerListingEntityBuilder() {
		return new DinnerListingEntityBuilder();
	}

	@Bean
	public EntityBuilder<Address, String> addressEntityBuilder() {
		return new AddressEntityBuilder();
	}

	@Bean
	public EntityBuilder<DinnerCart, String> dinnerCartEntityBuilder() {
		return new DinnerCartEntityBuilder();
	}

	@Bean
	public EntityBuilder<UserProfile, String> userProfileEntityBuilder() {
		return new UserProfileEntityBuilder();
	}

	@Bean
	public SimpleDateFormat simpleDateFormat() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		return simpleDateFormat;
	}

	@Bean
	public TaskScheduler taskScheduler() {
		ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
		taskScheduler.setPoolSize(10);
		return taskScheduler;
	}

	@Bean(name = "radarSearchRequest")
	@Scope("prototype")
	public PlacesSearchRequest radarSearchRequest() {
		return new RadarSearchRequest();
	}

	@Bean(name = "nearbySearchRequest")
	@Scope("prototype")
	public PlacesSearchRequest nearbySearchRequest() {
		return new NearbySearchRequest();
	}

	@Bean(name = "latlngSearchRequest")
	@Scope("prototype")
	public PlacesSearchRequest latlngSearchRequest() {
		return new LatLngSearchRequest();
	}

	@Bean(name = "placesServiceLocatorFactory")
	public ServiceLocatorFactoryBean placesServiceLocatorFactoryBean() {
		ServiceLocatorFactoryBean placesServiceLocatorFactoryBean = new ServiceLocatorFactoryBean();
		placesServiceLocatorFactoryBean.setServiceLocatorInterface(PlacesSearchRequestFactory.class);
		return placesServiceLocatorFactoryBean;
	}

	//@Bean
	//public PlacesSearchRequestFactory placesSearchRequestFactory() {
	//	return (PlacesSearchRequestFactory) placesServiceLocatorFactoryBean().getObject();
	//}

	@Bean
	public StripeDataService stripeDataService() {
		return new StripeDataService();
	}

	@Bean
	public PlaceOrderAspect placeOrderAspect() {
		return new PlaceOrderAspect();
	}

	@Bean
	public GCMSender gcmSender() {
		return new GCMSender();
	}

	@Bean
	public JacksonJsonProvider jsonProvider() {
		return new JacksonJsonProvider();
	}

	@Autowired
	private Environment env;

	@Bean
	public PlacesScheduledTask placeScheduledTask() {
		PlacesScheduledTask task = new PlacesScheduledTask();
		return task;
	}

	//@Bean
	//public ProcessOrderAspect processOrderAspect() {
	//	return new ProcessOrderAspect();
	//}

	@Bean
	public FilterProcessor<DinnerListing> dinnerFilterProcessor() {
		return new DinnerListingFilterProcessor();
	}


	@Bean
	@Profile("cloud")
	public ApnsService apnsService(StorageOperations storageOperations) throws P2PDinnerException {
		Path certificatePath = storageOperations.readObject("certificates/push_production.p12");
		ApnsService service = APNS.newService().withCert(certificatePath.toFile().getAbsolutePath(), "p2pdinner")
				.withProductionDestination().build();
		return service;
	}

	@Bean
	@Profile({"default", "development", "docker"})
	public ApnsService apnsServiceDevelopment(StorageOperations storageOperations) throws P2PDinnerException, IOException {
		Resource resource = new ClassPathResource(appleCertificatePath);
		Path certificatePath = Paths.get(resource.getURI());
		ApnsService service = APNS.newService().withCert(certificatePath.toFile().getAbsolutePath(), "p2pdinner")
				.withProductionDestination().build();
		return service;
	}
}
