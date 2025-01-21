package com.future.permission.model.permission;
import com.future.common.util.treeutil.SumTree;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


/**
 * 个人信息设置 我的组织/我的岗位/（我的角色：暂无）
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2022/1/25
 */
@Data
public class PermissionModel extends SumTree {

    @Schema(description ="名称")
    private String fullName;
    @Schema(description ="id")
    private String id;
    @Schema(description ="是否为默认")
    private Boolean isDefault;

}
