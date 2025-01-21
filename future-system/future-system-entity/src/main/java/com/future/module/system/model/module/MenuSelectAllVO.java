package com.future.module.system.model.module;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021/3/12 15:31
 */
@Data
public class MenuSelectAllVO {
    @Schema(description ="主键")
    private String id;
    @Schema(description ="是否有下级菜单")
    private Boolean hasChildren;
    @Schema(description ="上级ID")
    private String parentId;
    @Schema(description ="状态")
    private Integer enabledMark;
    @Schema(description ="菜单名称")
    private String fullName;
    @Schema(description =" 图标")
    private String icon;
    @Schema(description ="链接地址")
    private String urlAddress;
    @Schema(description ="菜单类型",example = "1")
    private Integer type;
    @Schema(description ="下级菜单列表")
    private List<MenuSelectAllVO> children;
    private Long sortCode;
    @Schema(description ="配置")
    private String propertyJson;

    @Schema(description ="外链")
    private String linkTarget;
    @Schema(description ="编码")
    private String enCode;

    @Schema(description ="系统id")
    private String systemId;
    @Schema(description = "是否菜单")
    private Boolean hasModule;
}
