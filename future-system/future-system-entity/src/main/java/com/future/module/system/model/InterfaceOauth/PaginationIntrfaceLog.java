package com.future.module.system.model.InterfaceOauth;

import com.future.common.base.PaginationTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 日志列表查询
 *
 * @author Future Platform Group
 * @version v4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2022/6/10 11:24
 */
@Data
public class PaginationIntrfaceLog extends PaginationTime {
    private String keyword;
}
