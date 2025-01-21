package com.future.module.system.model.cacheManage;

import com.future.common.base.Page;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2023年11月15日19:29:50
 */
@Data
public class PaginationCacheManage extends Page {
    @Schema(description = "开始时间")
    private Long overdueStartTime;
    @Schema(description = "结束时间")
    private Long overdueEndTime;
}
