package com.future.permission.model.usergroup;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

import com.future.common.base.Pagination;

/**
 * 用户分组管理列表返回
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date ：2022/3/11 8:58
 */
@Data
public class PaginationGroup extends Pagination implements Serializable {
    @Schema(description = "状态")
    private Integer enabledMark;
    @Schema(description = "类型")
    private String type;
}
