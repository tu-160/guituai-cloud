package com.future.filter;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.headers.HttpHeadersFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.future.common.base.ActionResultCode;
import com.future.common.constant.GlobalConst;
import com.future.common.consts.AuthConsts;
import com.future.common.properties.GatewayWhite;
import com.future.common.properties.SecurityProperties;
import com.future.common.util.UserProvider;
import com.future.reids.config.ConfigValueUtil;
import com.future.util.ReactorUtil;


/**
 * 网关验证token
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-03-24
 */
@Slf4j
@Configuration
@Component
public class AuthFilter {

    @Autowired
    private ConfigValueUtil configValueUtil;
    @Autowired
    private SecurityProperties securityProperties;


    // 注册 Sa-Token全局过滤器
    @Bean
    public SaReactorFilter getSaReactorFilter(GatewayWhite gatewayWhite) {
        return new SaReactorFilter()
            // 拦截地址
            .addInclude("/**")
            .setExcludeList(gatewayWhite.excludeUrl)
            // 鉴权方法：每次访问进入
            .setAuth(obj -> {
                if(log.isInfoEnabled()){
                    log.info("请求路径: {}", SaHolder.getRequest().getRequestPath());
                }
                //拦截路径
                SaRouter.match(gatewayWhite.blockUrl).match(o -> {
                    //禁止访问URL 排除白名单
                    String ip = getIpAddr();
                    for (String o1 : gatewayWhite.whiteIp) {
                        if(ip.startsWith(o1)){
                            return false;
                        }
                    }
                    log.info("非白名单IP访问限制接口：{}, {}", SaHolder.getRequest().getRequestPath(), ip);
                    return true;
                }).back("接口无法访问");
                //测试不验证 鉴权服务重启测试模式不清除Token就够了
                //SaRouter.match((r)->"true".equals(configValueUtil.getTestVersion())).stop();
                //白名单不拦截
                SaRouter.match(gatewayWhite.whiteUrl).stop();
                //内部请求不拦截
                SaRouter.match(t->{
                    String innerToken = SaHolder.getRequest().getHeader(AuthConsts.INNER_TOKEN_KEY);
                    return UserProvider.isValidInnerToken(innerToken);
                }).stop();
                // 登录校验 -- 校验多租户管理模块TOKEN
                SaRouter.match("/api/tenant/**", r -> {
                    SaManager.getStpLogic(AuthConsts.ACCOUNT_TYPE_TENANT).checkLogin();
                }).stop();
                // 登录校验 -- 拦截所有路由
                SaRouter.match("/**", r -> {
                    StpUtil.checkLogin();
                }).stop();
            }).setError(e -> {
                SaHolder.getResponse().addHeader("Content-Type","application/json; charset=utf-8");
                if(e instanceof NotLoginException){
                    return SaResult.error(ActionResultCode.SessionOverdue.getMessage()).setCode(ActionResultCode.SessionOverdue.getCode());
                }
                log.error(e.getMessage(), e);
                return SaResult.error("系统异常.").setCode(ActionResultCode.Exception.getCode());
            });
    }


    public static String getIpAddr() {
        return ReactorUtil.getIpAddr(SaHolder.getRequest());
    }

    /**
     * Header过滤器， 为请求添加 Id-Token
     * @return
     */
    @Bean
    public HttpHeadersFilter getMyHeadersFilter(){
        return (input, exchange) -> {
            if(securityProperties.isEnableInnerAuth() || securityProperties.isEnablePreAuth()){
                input.add(AuthConsts.INNER_GATEWAY_TOKEN_KEY, UserProvider.getInnerAuthToken());
            }
            input.add(GlobalConst.HEADER_HOST, input.getHost().toString());
            return input;
        };
    }

}
