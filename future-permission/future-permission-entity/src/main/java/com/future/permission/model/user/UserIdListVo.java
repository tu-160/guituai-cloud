package com.future.permission.model.user;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-05-29
 */
@Data
public class UserIdListVo extends UserBaseVO {

    /**
     * 前端协议字段，以后将改回realName
     */
    @Schema(description = "名称")
    private String fullName;

    @Schema(description = "头像")
    private String headIcon;

    @Schema(description = "性别")
    private String gender;

    @Schema(description = "手机号")
    private String mobilePhone;

    @Schema(description = "组织")
    private String organize;

    @Schema(description = "图标")
    private String icon;

    @Schema(description = "类型")
    private String type;


    @Schema(description ="组织id树")
    private List<String> organizeIds;

    @JsonIgnore
    @Schema(description = "类型")
    private Integer enabledMark;

    private Integer isAdministrator;
}
