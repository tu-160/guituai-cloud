package com.future.handler;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.context.model.SaRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.EmptyResponse;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.RoundRobinLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 负载均衡处理器
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2024-01-10
 */
@Slf4j
public class MyLoadBalancer extends RoundRobinLoadBalancer {

    private final String serviceId;
    private ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;
    private List<ILoadBalancerHandler> loadBalancers;


    public MyLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider, String serviceId, List<ILoadBalancerHandler> loadBalancers) {
        super(serviceInstanceListSupplierProvider, serviceId);
        this.serviceId = serviceId;
        this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
        this.loadBalancers = loadBalancers;
    }

    public MyLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider, String serviceId, int seedPosition, List<ILoadBalancerHandler> loadBalancers) {
        super(serviceInstanceListSupplierProvider, serviceId, seedPosition);
        this.serviceId = serviceId;
        this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
        this.loadBalancers = loadBalancers;
    }

    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {
        String uri = SaHolder.getRequest().getRequestPath();
        boolean hasSupport = loadBalancers.stream().anyMatch(l->l.isSupport(serviceId, uri));
        if(hasSupport) {
            SaRequest request1 = SaHolder.getRequest();
            ServiceInstanceListSupplier supplier = serviceInstanceListSupplierProvider
                    .getIfAvailable(NoopServiceInstanceListSupplier::new);
            return supplier.get(request).next()
                    .map(serviceInstances -> processInstanceResponse(supplier, serviceInstances, request1));
        }
        return super.choose(request);
    }

    private Response<ServiceInstance> processInstanceResponse(ServiceInstanceListSupplier supplier,
                                                              List<ServiceInstance> serviceInstances, SaRequest request) {
        if (serviceInstances.isEmpty()) {
            if (log.isWarnEnabled()) {
                log.warn("No servers available for service: " + serviceId);
            }
            return new EmptyResponse();
        }
        Response<ServiceInstance> response = null;
        for (ILoadBalancerHandler loadBalancer : loadBalancers) {
            response = loadBalancer.choose(serviceId, serviceInstances, request);
            if(response == null){
                break;
            }
        }
        return response == null ? new EmptyResponse() : response;
    }



}
