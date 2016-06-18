package com.p2p.data.config;

import com.p2p.common.config.P2PCommonContextConfig;
import com.p2p.domain.DinnerListing;
import com.p2p.filters.DinnerListingFilterProcessor;
import com.p2p.filters.FilterProcessor;
import com.p2p.scheduled.tasks.PlacesScheduledTask;
import com.p2p.services.aspects.ProcessOrderAspect;
import com.stormpath.sdk.client.Client;
import com.stormpath.sdk.client.Clients;
import com.stormpath.sdk.impl.api.ClientApiKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.text.SimpleDateFormat;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories
@ComponentScan(basePackages = {"com.p2p"})
@EnableScheduling
@EnableAsync
@PropertySource("classpath:/external-services.properties")
@Import({P2PCommonContextConfig.class})
public class P2PContextConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(P2PContextConfiguration.class);

    @Autowired
    private Environment env;

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

    @Bean
    public PlacesScheduledTask placeScheduledTask() {
        PlacesScheduledTask task = new PlacesScheduledTask();
        return task;
    }

    @Bean
    public ProcessOrderAspect processOrderAspect() {
        return new ProcessOrderAspect();
    }

    @Bean
    public FilterProcessor<DinnerListing> dinnerFilterProcessor() {
        return new DinnerListingFilterProcessor();
    }

    @Bean
    public Client stormPathClient() {
        return Clients.builder().setApiKey(new ClientApiKey(env.getProperty("stormpath.apiKey.id"), env.getProperty("stormpath.apiKey.secret"))).build();
    }

}
