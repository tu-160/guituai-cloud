package com.future.handler;

import cn.dev33.satoken.context.model.SaRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultResponse;
import org.springframework.cloud.client.loadbalancer.EmptyResponse;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.util.Assert;

import com.future.util.ReactorUtil;

import java.util.List;

/**
 * 同一个用户上传文件, 路由至同一台机器， 避免分片上传文件合并失败
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2024-01-10
 */
@Slf4j
public class FileChunkLoadBalancerHandler implements ILoadBalancerHandler{
    @Override
    public boolean isSupport(String serviceId, String uri) {
        return uri.endsWith("/chunk");
    }

    @Override
    public Response<ServiceInstance> choose(String serviceId, List<ServiceInstance> serviceInstances, SaRequest request) {
        //根据用户TOKEN路由
//        String routerKey = SaHolder.getRequest().getHeader(Constants.AUTHORIZATION);
//        if(StringUtil.isEmpty(routerKey)) {
//            routerKey = ReactorUtil.getIpAddr();
//        }
        //根据来访IP轮询
        String routerKey = ReactorUtil.getIpAddr(request);
        int hash = Math.max(routerKey.hashCode() % serviceInstances.size(), 0);
        ServiceInstance serviceInstance = serviceInstances.get(hash);
        if(serviceInstance == null){
            log.error("获取分片服务为空：{}, {}, {}", serviceId, hash, serviceInstances);
            return new EmptyResponse();
        }
        log.debug("file chunk route to: {}", serviceInstance.getInstanceId());
        return new DefaultResponse(serviceInstance);
    }
}
