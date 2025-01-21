package com.future.config;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.event.HeartbeatEvent;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.context.event.EventListener;

import com.github.xiaoymin.knife4j.spring.gateway.discover.ServiceChangeListener;
import com.github.xiaoymin.knife4j.spring.gateway.discover.ServiceDiscoverHandler;

public class MyServiceChangeListener extends ServiceChangeListener {

    @Autowired
    private DiscoveryClient discoveryClient;
    @Autowired
    private MyServiceDiscoverHandler serviceDiscoverHandler;

    public MyServiceChangeListener(DiscoveryClient discoveryClient, ServiceDiscoverHandler serviceDiscoverHandler) {
        super(discoveryClient, serviceDiscoverHandler);
    }

    @EventListener(classes = {ApplicationReadyEvent.class, HeartbeatEvent.class, RefreshRoutesEvent.class})
    public void discover() {
        List<String> services = discoveryClient.getServices();
        this.serviceDiscoverHandler.discover(services.stream().filter(t -> t.startsWith("future-") && !t.equals("future-datareport")).collect(Collectors.toList()));
    }
}
