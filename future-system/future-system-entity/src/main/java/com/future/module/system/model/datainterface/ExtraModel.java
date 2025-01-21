package com.future.module.system.model.datainterface;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "分页参数模型")
public class ExtraModel implements Serializable {
    @Schema(description = "字段名称")
    private String fieldName;
    @Schema(description = "字段名")
    private String field;
}
