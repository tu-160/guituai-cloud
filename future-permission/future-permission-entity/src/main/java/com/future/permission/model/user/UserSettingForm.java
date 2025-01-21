package com.future.permission.model.user;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 类功能
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2022/1/28
 */
@Data
public class UserSettingForm {

    @Schema(description ="主要类型")
    private String majorType;
    @Schema(description ="主要Id")
    private String majorId;

    @Schema(description ="菜单类型")
    private Integer menuType;

}
