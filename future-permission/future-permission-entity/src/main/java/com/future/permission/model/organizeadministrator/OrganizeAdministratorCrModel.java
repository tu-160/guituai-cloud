package com.future.permission.model.organizeadministrator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date ：2022/6/6 14:50
 */
@Data
public class OrganizeAdministratorCrModel implements Serializable {
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

    private Integer thisLayerAdd = 0;
    private Integer thisLayerEdit = 0;
    private Integer thisLayerDelete = 0;
    private Integer thisLayerSelect = 0;
    private Integer subLayerAdd = 0;
    private Integer subLayerEdit = 0;
    private Integer subLayerDelete = 0;
    private Integer subLayerSelect = 0;
    private String organizeIdTree;
    @JsonIgnore
    private String category;

    private List<OrganizeAdministratorCrModel> children;
}
