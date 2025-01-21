package com.future.module.system.model.province;

import com.future.common.base.Page;

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
public class PaginationProvince extends Page {

    @Schema(description = "状态")
    private Integer enabledMark;

}
