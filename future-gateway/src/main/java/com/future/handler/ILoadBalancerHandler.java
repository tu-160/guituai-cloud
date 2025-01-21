package com.future.handler;

import cn.dev33.satoken.context.model.SaRequest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.Response;

import java.util.List;

/**
 * 负载均衡处理器
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2024-01-10
 */
public interface ILoadBalancerHandler {

    boolean isSupport(String serviceId, String uri);

    Response<ServiceInstance> choose(String serviceId, List<ServiceInstance> serviceInstances, SaRequest request);


}
