package com.future.module.system.model.printdev;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PrintOption {
    @Schema(description = "主键")
    private String id;
    @Schema(description = "名称")
    private String fullName;
}
