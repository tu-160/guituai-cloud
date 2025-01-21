package com.future.permission.model.user;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

import com.future.common.base.Page;

/**
 * 通过组织id或关键字查询
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2022-01-13
 */
@Data
public class PageUser extends Page implements Serializable {
    @Schema(description = "组织id")
    private String organizeId;
}
