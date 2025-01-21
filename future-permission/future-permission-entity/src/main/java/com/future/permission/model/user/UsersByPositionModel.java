package com.future.permission.model.user;
import com.future.common.base.Page;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 获取岗位成员
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date ：2022/4/28 14:44
 */
@Data
public class UsersByPositionModel extends Page {
    @Schema(description = "岗位id")
    private String positionId;
}
