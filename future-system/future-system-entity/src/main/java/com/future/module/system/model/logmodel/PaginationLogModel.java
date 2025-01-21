package com.future.module.system.model.logmodel;

import com.future.common.base.PaginationTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 日志分页参数
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date ：2022/3/18 9:50
 */
@Data
public class PaginationLogModel extends PaginationTime {
    /**
     * 操作类型
     */
    private String requestMethod;
    /**
     * 类型
     */
    private int category;
    @Schema(description = "是否登录成功标志")
    private Integer loginMark;
    @Schema(description = "登录类型")
    private Integer loginType;

}
