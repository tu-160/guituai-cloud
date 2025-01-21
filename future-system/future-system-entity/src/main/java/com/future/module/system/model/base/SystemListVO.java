package com.future.module.system.model.base;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

import com.future.common.constant.PlatformConst;

/**
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date ：2022/6/21 15:50
 */
@Data
public class SystemListVO implements Serializable {

    @Schema(description = "主键")
    private String id;

    @Schema(description = "名称")
    private String fullName;

    @Schema(description = "编码")
    private String enCode;

    @Schema(description = "图标")
    private String icon;

    @Schema(description = "排序码")
    private Long sortCode;

    @Schema(description = "有效标志")
    private Integer enabledMark;

    @Schema(description = "扩展属性")
    private String propertyJson;

    @Schema(description = "是否主系统")
    private Integer isMain;
    @Schema(description = "是否为开发平台")
    private boolean mainSystem;
    @Schema(description = "导航图片")
    private String navigationIcon;
    @Schema(description = "Logo图片")
    private String workLogoIcon;

    public boolean isMainSystem() {
        if (PlatformConst.MAIN_SYSTEM_CODE.equals(this.getEnCode())) {
            return true;
        }
        return false;
    }

}
