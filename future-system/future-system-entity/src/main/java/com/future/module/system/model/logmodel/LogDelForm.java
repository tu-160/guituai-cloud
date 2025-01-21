package com.future.module.system.model.logmodel;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LogDelForm  {
    @Schema(description = "id集合")
    private String[] ids;
}

