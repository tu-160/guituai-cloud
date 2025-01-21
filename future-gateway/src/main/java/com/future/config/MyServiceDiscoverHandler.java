package com.future.config;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.support.NameUtils;
import org.springframework.core.env.Environment;

import com.github.xiaoymin.knife4j.spring.gateway.Knife4jGatewayProperties;
import com.github.xiaoymin.knife4j.spring.gateway.discover.ServiceDiscoverHandler;
import com.github.xiaoymin.knife4j.spring.gateway.enums.OpenApiVersion;
import com.github.xiaoymin.knife4j.spring.gateway.spec.v2.OpenAPI2Resource;
import com.github.xiaoymin.knife4j.spring.gateway.utils.PathUtils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyServiceDiscoverHandler extends ServiceDiscoverHandler {

    /**
     * Knife4j gateway properties
     */
    @Autowired
    private Knife4jGatewayProperties gatewayProperties;

    /**
     * 聚合内容
     */
    @Getter
    private List<OpenAPI2Resource> gatewayResources;

    @Autowired
    private GatewayProperties gatewayPropertiess;

    /**
     * Spring Environment
     */
    private Environment environment;

    public MyServiceDiscoverHandler(Knife4jGatewayProperties gatewayProperties) {
        super(gatewayProperties);
    }

    /**
     * 处理注册中心的服务
     * @param service 服务列表集合
     */
    public void discover(List<String> service) {

        log.debug("service has change.");
        Set<String> excludeService = getExcludeService();
        // 版本
        OpenApiVersion apiVersion = this.gatewayProperties.getDiscover().getVersion();
        // 判断当前类型
        String url = this.gatewayProperties.getDiscover().getUrl();
        // 个性化服务的配置信息
        Map<String, Knife4jGatewayProperties.ServiceConfigInfo> configInfoMap = this.gatewayProperties.getDiscover().getServiceConfig();
        List<OpenAPI2Resource> resources = new ArrayList<>();
        if (service != null && !service.isEmpty()) {
            for (String serviceName : service) {
                if (!serviceName.startsWith("future-") || serviceName.equals("future-datareport") || excludeService.contains(serviceName)) {
                    continue;
                }
                int order = 0;
                String groupName = serviceName;
                String contextPath = "/";
//                Knife4jGatewayProperties.ServiceConfigInfo serviceConfigInfo = configInfoMap.get(serviceName);
//                if (serviceConfigInfo != null) {
//                    order = serviceConfigInfo.getOrder();
//                    groupName = serviceConfigInfo.getGroupName();
//                    contextPath = PathUtils.append(contextPath, serviceConfigInfo.getContextPath());
//                }
                OpenAPI2Resource resource = new OpenAPI2Resource(order, true);
                resource.setName(groupName);
                RouteDefinition routeDefinition = gatewayPropertiess.getRoutes().stream().filter(t -> t.getId().equals(serviceName)).findFirst().orElse(new RouteDefinition());
                String replace = routeDefinition.getPredicates().get(0).getArgs().get(NameUtils.GENERATED_NAME_PREFIX + "0").replace("/**", "");
                resource.setContextPath(replace);
//                // 判断版本
//                if (apiVersion == OpenApiVersion.OpenAPI3) {
//                    if (contextPath.equalsIgnoreCase("/")) {
//                        // 自动追加一个serviceName
//                        resource.setContextPath("/" + serviceName);
//                    }
//                }
                resource.setUrl(PathUtils.append(PathUtils.append(replace, url), contextPath + serviceName));
                resource.setId(Base64.getEncoder().encodeToString((resource.getName() + resource.getUrl() + resource.getContextPath()).getBytes(StandardCharsets.UTF_8)));
                resources.add(resource);
            }
        }
        // 在添加自己的配置的个性化的服务
        if (this.gatewayProperties.getRoutes() != null) {
            for (Knife4jGatewayProperties.Router router : this.gatewayProperties.getRoutes()) {
                OpenAPI2Resource resource = new OpenAPI2Resource(router.getOrder(), false);
                resource.setName(router.getName());
                // 开发者配什么就返回什么
                resource.setUrl(router.getUrl());
                resource.setContextPath(router.getContextPath());
                resource.setId(Base64.getEncoder().encodeToString((resource.getName() + resource.getUrl() + resource.getContextPath()).getBytes(StandardCharsets.UTF_8)));
                resources.add(resource);
            }
        }
        // 赋值
        this.gatewayResources = resources;
    }

}
