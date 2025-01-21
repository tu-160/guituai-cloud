package com.future.permission.model.role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

import com.future.common.util.treeutil.SumTree;

/**
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021/3/12 15:31
 */
@Data
public class RoleSelectorVO {

    @Schema(description ="ID")
    private String id;
    @Schema(description ="名称")
    private String fullName;
    @Schema(description ="类型")
    private String type;
    @Schema(description ="数量")
    private Long num;
    @Schema(description ="前端解析唯一标识")
    private String onlyId;
    @Schema(description ="父节点ID")
    private String parentId;
    @Schema(description ="子类对象集合")
    private List<RoleSelectorVO> children;
    @Schema(description ="是否含有子类对象集合")
    private Boolean hasChildren;
    @Schema(description ="")
    private Boolean isLeaf;
    @Schema(description = "图标")
    private String icon;

    @Schema(description = "组织")
    private String organize;
    @Schema(description ="组织id树")
    private List<String> organizeIds;

}
