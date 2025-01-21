package com.future.permission.rest;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.future.common.util.JsonUtil;
import com.future.permission.connector.HttpRequestUserInfoService;
import com.future.permission.entity.UserEntity;

import lombok.extern.slf4j.Slf4j;

/**
 * 推送工具类
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date ：2022/7/28 20:56
 */
@Slf4j
@Component
public class PullUserUtil {


    private static HttpRequestUserInfoService httpRequestUserInfoService;

    public PullUserUtil(@Autowired(required = false) HttpRequestUserInfoService httpRequestUserInfoService){
        PullUserUtil.httpRequestUserInfoService = httpRequestUserInfoService;
    }

    /**
     * 推送到
     *
     * @param userEntity
     * @param method
     * @param tenantId
     */
    public static void syncUser(UserEntity userEntity, String method, String tenantId) {
        if (httpRequestUserInfoService != null) {
            Map<String, Object> map = JsonUtil.entityToMap(userEntity);
            httpRequestUserInfoService.syncUserInfo(map, method, tenantId);
        }
    }

}
