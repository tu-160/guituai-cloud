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
public class UserAllVO {
    @Schema(description ="主键")
    private String id;
    @Schema(description ="账号")
    private String account;
    @Schema(description ="名称")
    private String realName;
    @Schema(description ="用户头像")
    private String headIcon;
    /**
     * //1,男。2女
     */
    @Schema(description ="性别")
    private String gender;
    //    @Schema(description ="部门")
//    private String department;
    @Schema(description ="快速搜索")
    private String quickQuery;
}
