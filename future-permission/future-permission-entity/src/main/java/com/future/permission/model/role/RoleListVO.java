package com.future.permission.model.role;
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
public class RoleListVO {
    @Schema(description ="主键")
    private String id;
    @Schema(description ="名称")
    private String fullName;
    @Schema(description ="编码")
    private String enCode;
    @Schema(description ="角色类型")
    private String type;
    @Schema(description ="所属组织")
    private String organizeInfo;
    @Schema(description ="备注")
    private String description;
    @Schema(description ="状态")
    private Integer enabledMark;
    private Long creatorTime;
    @Schema(description ="排序")
    private Long sortCode;
}
