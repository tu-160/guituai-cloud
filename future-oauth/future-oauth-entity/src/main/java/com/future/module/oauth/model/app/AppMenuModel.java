package com.future.module.oauth.model.app;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AppMenuModel {
    @Schema(description ="扩展字段")
    private String propertyJson;
    @Schema(description ="菜单编码")
    private String enCode;
    @Schema(description ="菜单名称")
    private String fullName;
    @Schema(description ="图标")
    private String icon;
    @Schema(description ="主键id")
    private String id;
    @Schema(description ="链接地址")
    private String urlAddress;

}
