package com.future.permission.model.organize;

import com.future.common.base.Page;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PaginationOrganize extends Page {

    @Schema(description = "状态")
    private Integer enabledMark;

    private String type;

}
