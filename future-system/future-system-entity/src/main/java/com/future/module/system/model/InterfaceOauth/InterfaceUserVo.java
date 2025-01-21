package com.future.module.system.model.InterfaceOauth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 授权用户展示
 *
 * @author Future Platform Group
 * @version v4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021/9/20 9:22
 */
@Data
public class InterfaceUserVo {

    @Schema(description = "用户id")
    private String userId;

    @Schema(description = "用户名称")
    private String userName;

    @Schema(description = "用户密钥")
    private String userKey;
}
