package com.future.module.system.model.printdev;

import com.future.common.base.Pagination;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-11-20
 */
@Data
public class PrintPagination extends Pagination {
    private String category;
    @Schema(description = "状态")
    private Integer enabledMark;
}
