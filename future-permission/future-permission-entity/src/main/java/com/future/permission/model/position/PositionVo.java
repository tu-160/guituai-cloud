package com.future.permission.model.position;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 通过组织id获取岗位列表
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-12-21
 */
@Data
public class PositionVo implements Serializable {
    private String id;

    @Schema(description ="名称")
    private String  fullName;
}
