package com.future.permission.model.organizeadministrator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021/3/12 15:31
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrganizeAdministratorSelectorVO implements Serializable {
    @Schema(description ="主键")
    private String organizeId;
    @Schema(description ="名称")
    private String fullName;
    @Schema(description ="图标")
    private String icon;
    @Schema(description ="")
    private Boolean isLeaf;
    @Schema(description ="是否有下级菜单")
    private Boolean hasChildren;
    private String parentId;

    private Integer thisLayerAdd;
    private Integer thisLayerEdit;
    private Integer thisLayerDelete;
    private Integer thisLayerSelect;
    private Integer subLayerAdd;
    private Integer subLayerEdit;
    private Integer subLayerDelete;
    private Integer subLayerSelect;
    private String organizeIdTree;
    @JsonIgnore
    private String category;

    private List<OrganizeAdministratorSelectorVO> children;
}
