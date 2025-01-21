package com.future.module.system.model.InterfaceOauth;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 接口认证列表对象
 *
 * @author Future Platform Group
 * @version v4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2022/7/6 10:50
 */
@Data
public class InterfaceIdentListVo {
    @Schema(description ="id")
    private String id;

    @Schema(description ="应用id")
    private String appId;

    @Schema(description ="应用名称")
    private String appName;

    @Schema(description ="使用期限")
    private Long usefulLife;

    @Schema(description ="创建人id")
    private String creatorUserId;

    @Schema(description ="创建人")
    private String creatorUser;

    @Schema(description ="创建时间")
    private Long creatorTime;

    @Schema(description ="修改时间")
    private Long lastModifyTime;

    @Schema(description ="排序")
    private Long sortCode;

    @Schema(description ="状态")
    private Integer enabledMark;

    @Schema(description ="租户id")
    private String tenantId;

    @Schema(description ="绑定接口")
    private String dataInterfaceIds;
}
