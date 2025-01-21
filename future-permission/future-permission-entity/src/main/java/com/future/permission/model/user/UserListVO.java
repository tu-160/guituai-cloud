package com.future.permission.model.user;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021/3/12 15:31
 */
@Data
public class UserListVO {
    @Schema(description ="主键")
    private String id;
    @Schema(description ="账号")
    private String account;
    @Schema(description ="姓名")
    private String realName;
    @Schema(description ="性别")
    private String gender;
    @Schema(description ="手机")
    private String mobilePhone;
    @Schema(description ="部门")
    private String organize;
    @Schema(description ="说明")
    private String description;
    @Schema(description ="状态")
    private Integer enabledMark;
    @Schema(description ="添加时间",example = "1")
    private Long creatorTime;
    @Schema(description ="排序")
    private Long sortCode;
    @Schema(description ="锁定标志")
    private Integer lockMark;
    @Schema(description ="交接状态")
    private Integer handoverMark;
    private Integer isAdministrator;
    private String headIcon;
}
