package com.future.permission.model.user;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date ：2022/4/14 15:45
 */
@Data
public class UserByRoleModel implements Serializable {
    /**
     * 关键字
     */
    @Schema(description = "关键字")
    private String keyword;

    /**
     * 组织id
     */
    @Schema(description = "组织id")
    private String organizeId;

    /**
     * 角色id
     */
    @Schema(description = "角色id")
    private String roleId;

}
