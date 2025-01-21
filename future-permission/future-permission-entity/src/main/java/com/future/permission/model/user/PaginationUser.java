package com.future.permission.model.user;
import com.future.common.base.Pagination;

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
public class PaginationUser extends Pagination {

    @Schema(description = "组织id")
    private String organizeId;

    @Schema(description = "角色id")
    private String roleId;
    @Schema(description = "状态")
    private Integer enabledMark;
    @Schema(description = "性别")
    private String gender;

}
