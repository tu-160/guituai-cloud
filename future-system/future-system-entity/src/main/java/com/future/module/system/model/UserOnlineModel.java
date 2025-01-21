package com.future.module.system.model;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 在线用户
 */
@Data
public class UserOnlineModel {
    private String userId;
    private String userAccount;
    private String userName;
    private String loginTime;
    private String loginIPAddress;
    private String loginSystem;
    private String tenantId;
    private String token;
    private String device;
    private String organize;
    private String loginBrowser;
    private String loginAddress;
}
