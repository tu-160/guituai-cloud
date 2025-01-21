package com.future.permission.model.user;
import com.future.common.base.Pagination;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserIdModelByPage extends UserIdModel {

    @Schema(description = "分页参数")
    private Pagination pagination;

    @Schema(description = "类型")
    private String type;
    
}
