package com.future.permission.model.user;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

import com.future.common.base.Pagination;

@Data
public class UserLogForm extends Pagination implements Serializable {
    @Schema(description = "开始时间")
    private String startTime;
    @Schema(description = "结束时间")
    private String endTime;
    @Schema(description = "分类")
    private int category;
    @Schema(description = "是否登录成功标志")
    private Integer loginMark;
    @Schema(description = "登录类型")
    private Integer loginType;
}
