package com.future.module.system.model.InterfaceOauth;

import com.future.common.base.Pagination;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 接口认证查询参数
 *
 * @author Future Platform Group
 * @version v4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2022/6/8 10:33
 */
@Data
public class PaginationOauth extends Pagination {
    @Schema(description = "有效标志")
    private Integer enabledMark;
}
