package com.future.module.system.model.logmodel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021/3/16 10:10
 */
@Data
public class ErrorLogVO {
    @Schema(description = "创建用户")
    private String userName;
    @Schema(description = "创建时间",example = "1")
    private Long creatorTime;
    @Schema(description = "IP")
    private String ipAddress;
    private Long requestDuration;
    @Schema(description = "地点")
    private String ipAddressName;
    @Schema(description = "id")
    private String id;
    @Schema(description = "请求设备")
    private String platForm;
    @Schema(description = "浏览器")
    private String browser;
    @Schema(description = "请求类型")
    private String requestMethod;
    @Schema(description = "请求地址")
    private String requestUrl;
}
