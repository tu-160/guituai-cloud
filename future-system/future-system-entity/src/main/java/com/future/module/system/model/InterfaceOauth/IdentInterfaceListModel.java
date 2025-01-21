package com.future.module.system.model.InterfaceOauth;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 授权接口列表
 *
 * @author Future Platform Group
 * @version v4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2022/6/9 18:12
 */
@Data
public class IdentInterfaceListModel {

    @Schema(description = "接口认证id")
    private String interfaceIdentId;

    @Schema(description = "接口id")
    private String dataInterfaceIds;
}
