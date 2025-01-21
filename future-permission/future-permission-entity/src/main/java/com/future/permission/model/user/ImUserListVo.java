package com.future.permission.model.user;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * IM获取用户接口
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-05-29
 */
@Data
public class ImUserListVo {
    @Schema(description ="主键")
    private String id;
    @Schema(description ="名称")
    private String realName;
    @Schema(description ="用户头像")
    private String headIcon;
    @Schema(description ="部门")
    private String department;
    @Schema(description ="账号")
    private String account;
}
