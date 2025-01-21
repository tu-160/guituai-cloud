package com.future.permission.model.organizeadministrator;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 组织关系表模型
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date ：2022/5/30 9:23
 */
@Data
public class OrganizeAdministratorListVo implements Serializable {
    @Schema(description = "主键")
    private String id;
    @Schema(description = "账号")
    private String account;
    @Schema(description = "真实姓名")
    private String realName;
    @Schema(description = "性别")
    private String gender;
    @Schema(description = "手机号")
    private String mobilePhone;
    @Schema(description = "组织id")
    private String organizeId;
    @Schema(description = "创建时间")
    private Long creatorTime;

}
