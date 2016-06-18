package com.p2p.data.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.p2p.common.config.P2PCommonContextConfig;
import com.p2p.common.notifications.PushNotificationFactory;
import com.p2p.data.service.PlaceOrderAspect;
import com.p2p.data.service.StripeDataService;
import com.p2p.domain.DinnerCart;
import com.p2p.domain.DinnerListing;
import com.p2p.domain.MenuItem;
import com.p2p.domain.UserProfile;
import com.p2p.domain.vo.*;
import com.p2p.rest.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.beans.factory.config.ServiceLocatorFactoryBean;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories
@ComponentScan(basePackages = { "com.p2p" })
@EnableScheduling
@EnableAsync
@EnableAspectJAutoProxy
@PropertySource("classpath:/external-services.properties")
public class P2PDataContextConfiguration {

	private static final Logger LOGGER = LoggerFactory.getLogger(P2PDataContextConfiguration.class);

	@Value("#{environment.DATABASE_URL}")
	private String jdbcUrl;

	@Value("#{environment.DATABASE_USERNAME}")
	private String databaseUserName;

	@Value("#{environment.DATABASE_PASSWORD}")
	private String databasePassword;

	@Value("#{environment.DATABASE_HOST_NAME}")
	private String databaseHostName;

	@Value("#{environment.DATABASE_PORT}")
	private Integer databasePort;

	@Value("#{environment.DATABASE}")
	private String database;

	@Bean
	public Properties propertiesFactory() throws IOException {
		Resource resource = new ClassPathResource("datasource.properties");
		LOGGER.info("Set configuration file location to => " + resource.getFile().getAbsolutePath());
		PropertiesFactoryBean propertiesFactory = new PropertiesFactoryBean();
		propertiesFactory.setLocation(resource);
		propertiesFactory.afterPropertiesSet();
		return (Properties) propertiesFactory.getObject();
	}

	@Bean
	public FactoryBean<EntityManagerFactory> entityManagerFactory(DataSource dataSource, JpaVendorAdapter jpaVendorAdapter) {
		LocalContainerEntityManagerFactoryBean emfb = new LocalContainerEntityManagerFactoryBean();
		emfb.setDataSource(dataSource);
		emfb.setJpaVendorAdapter(jpaVendorAdapter);
		emfb.setPackagesToScan("com.p2p.domain");
		return emfb;
	}

	@Bean
	public JpaVendorAdapter jpaVendorAdapter() {
		HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
		jpaVendorAdapter.setDatabase(Database.POSTGRESQL);
		jpaVendorAdapter.setGenerateDdl(true);
		jpaVendorAdapter.setShowSql(true);
		return jpaVendorAdapter;
	}

	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory, DataSource dataSource) throws Exception {
		JpaTransactionManager txManager = new JpaTransactionManager();
		txManager.setEntityManagerFactory(entityManagerFactory);
		txManager.setDataSource(dataSource);
		return txManager;
	}

	@Bean
	public DataSource dataSource() throws IOException, Exception {
		Properties properties = propertiesFactory();
		ComboPooledDataSource dataSource = new ComboPooledDataSource();
		dataSource.setDriverClass(properties.getProperty(ConfigurationConstants.P2PDINNER_DS_DRIVERCLASS.getName()));
		String jdbcUrl = new StringBuffer("jdbc:postgresql://").append(databaseHostName).append(":").append(databasePort).append("/").append(database).toString();
		dataSource.setJdbcUrl(jdbcUrl);
		dataSource.setUser(databaseUserName);
		dataSource.setPassword(databasePassword);
		dataSource.setMaxPoolSize(
				Integer.parseInt(properties.getProperty(ConfigurationConstants.P2PDINNER_DS_MAXPOOLSIZE.getName())));
		dataSource.setAcquireIncrement(Integer
				.parseInt(properties.getProperty(ConfigurationConstants.P2PDINNER_DS_ACQUIREINCREMENTS.getName())));
		dataSource.setMaxStatements(
				Integer.parseInt(properties.getProperty(ConfigurationConstants.P2PDINNER_DS_MAXSTATEMENTS.getName())));
		dataSource.setMaxStatements(
				Integer.parseInt(properties.getProperty(ConfigurationConstants.P2PDINNER_DS_MINPOOLSIZE.getName())));
		return dataSource;
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
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setRequestFactory(new SimpleClientHttpRequestFactory());
		return restTemplate;
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

	@Bean
	public PlacesSearchRequestFactory placesSearchRequestFactory() {
		return (PlacesSearchRequestFactory) placesServiceLocatorFactoryBean().getObject();
	}

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

}
