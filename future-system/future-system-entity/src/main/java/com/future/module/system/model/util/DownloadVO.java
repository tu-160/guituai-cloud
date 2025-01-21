package com.future.module.system.model.util;


import lombok.AllArgsConstructor;
import lombok.Builder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DownloadVO {
    @Schema(description ="名称")
    private String name;
    @Schema(description ="请求接口")
    private String url;
}
