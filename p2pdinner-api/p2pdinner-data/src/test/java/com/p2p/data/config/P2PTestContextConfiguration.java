package com.p2p.data.config;

import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import com.p2p.common.notifications.PushNotificationFactory;
import com.p2p.rest.client.GCMSender;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

import com.p2p.data.service.MenuDataService;
import com.p2p.data.service.P2PDataService;
import com.p2p.data.service.StripeDataService;
import com.p2p.data.service.UserProfileDataService;
import com.p2p.domain.DinnerCart;
import com.p2p.domain.DinnerListing;
import com.p2p.domain.MenuItem;
import com.p2p.domain.vo.Address;
import com.p2p.domain.vo.AddressEntityBuilder;
import com.p2p.domain.vo.DinnerCartEntityBuilder;
import com.p2p.domain.vo.DinnerListingEntityBuilder;
import com.p2p.domain.vo.DinnerListingVO;
import com.p2p.domain.vo.EntityBuilder;
import com.p2p.domain.vo.MenuItemEntityBuilder;
import com.p2p.domain.vo.MenuItemVO;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = { "com.p2p.data.repositories", "com.p2p.domain" })
@ComponentScan(basePackages = "com.p2p")
@PropertySource("classpath:/external-services.properties")
public class P2PTestContextConfiguration {

	private static final Logger LOGGER = LoggerFactory.getLogger(P2PTestContextConfiguration.class);

	@Autowired
	private DataSource dataSource;

	@Autowired
	private EntityManagerFactory entityManagerFactory;

	@Bean
	public FactoryBean<EntityManagerFactory> entityManagerFactory() throws IOException {
		LocalContainerEntityManagerFactoryBean emfb = new LocalContainerEntityManagerFactoryBean();
		emfb.setDataSource(this.dataSource);
		Resource persistenceXmlResource = new ClassPathResource("/META-INF/test-persistence.xml");
		emfb.setPersistenceXmlLocation("/META-INF/test-persistence.xml");
		LOGGER.info("### Persistence Xml location {}", persistenceXmlResource.getFile().getAbsolutePath());
		emfb.setPersistenceUnitName("p2pdinner-test");
		emfb.setJpaVendorAdapter(jpaVendorAdapter());
		return emfb;
	}

	@Bean
	public JpaVendorAdapter jpaVendorAdapter() {
		return new HibernateJpaVendorAdapter();
	}

	@Bean
	public PlatformTransactionManager transactionManager() {
		JpaTransactionManager txManager = new JpaTransactionManager();
		txManager.setEntityManagerFactory(this.entityManagerFactory);
		txManager.setDataSource(this.dataSource);
		return txManager;
	}

	@Bean
	public DataSource dataSource() throws IOException, Exception {
		EmbeddedDatabase database = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build();
		return database;
	}

	@Bean
	public P2PDataService p2PDataService() {
		P2PDataService p2PDataService = new P2PDataService();
		return p2PDataService;
	}

	@Bean
	public EntityBuilder<MenuItem, MenuItemVO> menuEntityBuilder() {
		EntityBuilder<MenuItem, MenuItemVO> dinnerListingEntityBuilder = new MenuItemEntityBuilder();
		return dinnerListingEntityBuilder;
	}

	@Bean
	public EntityBuilder<DinnerListing, DinnerListingVO> dinnerListingEntityBuilder() {
		EntityBuilder<DinnerListing, DinnerListingVO> dinnerListingEntityBuilder = new DinnerListingEntityBuilder();
		return dinnerListingEntityBuilder;
	}
	
	@Bean
	public EntityBuilder<DinnerCart, String> dinnerCartEntityBuilder() {
		EntityBuilder<DinnerCart, String> dinnerCartEntityBuilder = new DinnerCartEntityBuilder();
		return dinnerCartEntityBuilder;
	}
	
	@Bean
	public EntityBuilder<Address, String> addressEntityBuilder(){
		EntityBuilder<Address,String> addressEntityBuilder = new AddressEntityBuilder();
		return addressEntityBuilder;
	}
	
	@Bean
	public SimpleDateFormat simpleDateFormat() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		return simpleDateFormat;
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	@Bean
	public UserProfileDataService userProfileDataService() {
		return new UserProfileDataService();
	}
	
	@Bean
	public MenuDataService menuDataService(){
		return new MenuDataService();
	}
	
	@Bean
	public StripeDataService stripeDataService() {
		return Mockito.mock(StripeDataService.class);
	}

	@Bean
	public PushNotificationFactory pushNotificationFactory() {
		return Mockito.mock(PushNotificationFactory.class);
	}

	@Bean
	public GCMSender gcmSender() { return new GCMSender();}
}
