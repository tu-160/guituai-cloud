package com.future.permission.model.user;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

import com.future.common.base.Pagination;

/**
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date ：2022/6/6 17:33
 */
@Data
public class UserConditionModel implements Serializable {

    /**
     * 部门id
     */
    @Schema(description = "部门id")
    private List<String> departIds;

    /**
     * 岗位id
     */
    @Schema(description = "岗位id")
    private List<String> positionIds;

    /**
     * 用户id
     */
    @Schema(description = "用户id")
    private List<String> userIds;

    /**
     * 角色Id
     */
    @Schema(description = "角色Id")
    private List<String> roleIds;

    /**
     * 分组Id
     */
    @Schema(description = "分组Id")
    private List<String> groupIds;

    /**
     * 类型
     */
    @Schema(description = "类型")
    private String type;

    @Schema(description = "分页参数")
    private Pagination pagination;

}
