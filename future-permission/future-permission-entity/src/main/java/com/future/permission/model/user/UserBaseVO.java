package com.future.permission.model.user;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户视图对象基类
 *
 * @author Future Platform Group
 * @version v4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2022/2/23
 */
@Data
public class UserBaseVO {

    @Schema(description ="主键")
    private String id;
    @Schema(description ="账号")
    private String account;
    @Schema(description ="名称")
    private String realName;

}
