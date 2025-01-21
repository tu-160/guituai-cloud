package com.future.permission.model.organizeadministrator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.future.common.util.treeutil.SumTree;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date ：2022/6/6 10:16
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrganizeAdministratorSelectorModel extends SumTree {
    @Schema(description ="组织主键")
    private String organizeId;
    @Schema(description ="名称")
    private String fullName;
    @Schema(description ="图标")
    private String icon;

    private Integer thisLayerAdd;
    private Integer thisLayerEdit;
    private Integer thisLayerDelete;
    private Integer thisLayerSelect;
    private Integer subLayerAdd;
    private Integer subLayerEdit;
    private Integer subLayerDelete;
    private Integer subLayerSelect;
    private String organizeIdTree;
    @Schema(description ="")
    private Boolean isLeaf;
    @JsonIgnore
    private String category;
}
