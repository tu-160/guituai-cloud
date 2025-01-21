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
public class LoginLogVO {
    @Schema(description = "id")
    private String id;
    @Schema(description = "创建时间",example = "1")
    private Long creatorTime;
    @Schema(description = "登陆用户")
    private String userName;
    @Schema(description = "登陆IP")
    private String ipAddress;
    @Schema(description = "登陆平台")
    private String platForm;
    @Schema(description = "登陆日志摘要")
    private String abstracts;
    @Schema(description = "地点")
    private String ipAddressName;
    @Schema(description = "浏览器")
    private String browser;
    @Schema(description = "是否登录成功标志")
    private Integer loginMark;
    @Schema(description = "登录类型")
    private Integer loginType;

    /**
     * 请求耗时
     */
    public int requestDuration;
}
