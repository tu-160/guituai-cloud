package com.future.config;

import com.github.xiaoymin.knife4j.spring.gateway.Knife4jGatewayProperties;
import com.github.xiaoymin.knife4j.spring.gateway.discover.ServiceChangeListener;
import com.github.xiaoymin.knife4j.spring.gateway.discover.ServiceDiscoverHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;

@Configuration
public class Knife4jConfig {


    /**
     * Knife4j gateway properties
     */
    @Autowired
    private Knife4jGatewayProperties gatewayProperties;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Primary
    @Bean
    @Lazy(false)
    public ServiceChangeListener myServiceChangeListener() {
        return new MyServiceChangeListener(discoveryClient,myServiceDiscoverHandler());
    }

    @Primary
    @Bean
    @Lazy(false)
    public ServiceDiscoverHandler myServiceDiscoverHandler() {
        return new MyServiceDiscoverHandler(gatewayProperties);
    }

}
