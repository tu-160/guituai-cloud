package com.future.config;

import com.alibaba.csp.sentinel.adapter.gateway.sc.SentinelGatewayFilter;
import com.future.handler.FileChunkLoadBalancerHandler;
import com.future.handler.ILoadBalancerHandler;
import com.future.handler.SentinelHandler;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * 网关限流配置
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-03-24
 */
@Configuration
@LoadBalancerClients(defaultConfiguration = {MyLoadBalancerConfig.class})
public class GatewayConfig
{
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SentinelHandler sentinelGatewayExceptionHandler()
    {
        return new SentinelHandler();
    }

    @Bean
    @Order(-1)
    public GlobalFilter sentinelGatewayFilter()
    {
        return new SentinelGatewayFilter();
    }

    @Bean
    public ILoadBalancerHandler getFileChunkLoadBalancerHandler(){
        return new FileChunkLoadBalancerHandler();
    }
}
