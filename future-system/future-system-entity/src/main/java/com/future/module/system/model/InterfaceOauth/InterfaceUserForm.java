package com.future.module.system.model.InterfaceOauth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 授权用户表单
 *
 * @author Future Platform Group
 * @version v4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021/9/20 9:22
 */
@Data
public class InterfaceUserForm {

    @Schema(description = "接口认证id")
    private String interfaceIdentId;

    @Schema(description = "授权用户列表")
    private List<String> userIds;
}
