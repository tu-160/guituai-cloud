package com.future.module.oauth.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;


/**
 * 通用登录配置
 * 是否跳转
 * 第三方登录配置
 *
 * @author Future Platform Group
 * @copyright 直方信息科技有限公司
 */
@Data
public class LoginConfigModel<T> {

    /**
     * 是否跳转
     */
    @Schema(description = "是否跳转")
    private boolean redirect = false;

    /**
     * 跳转URL地址
     */
    @Schema(description = "跳转URL地址")
    private String url;

    /**
     * 跳转登录轮询票据
     */
    @Schema(description = "跳转登录轮询票据")
    private String ticketParams;


    /**
     * 第三方登录列表
     */
    @Schema(description = "第三方登录列表")
    List<T> socialsList;


}
