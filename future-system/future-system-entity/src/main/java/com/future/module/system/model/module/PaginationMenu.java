package com.future.module.system.model.module;


import com.future.common.base.Page;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PaginationMenu extends Page {
   @Schema(description = "分类")
   private String category;
   @Schema(description = "状态")
   private Integer enabledMark;
   @Schema(description = "类型")
   private Integer type;
}
