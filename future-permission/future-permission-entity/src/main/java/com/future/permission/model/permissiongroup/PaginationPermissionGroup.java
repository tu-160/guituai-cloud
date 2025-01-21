package com.future.permission.model.permissiongroup;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

import com.future.common.base.Pagination;

@Data
public class PaginationPermissionGroup extends Pagination implements Serializable {
    @Schema(description = "状态")
    private Integer enabledMark;
}
