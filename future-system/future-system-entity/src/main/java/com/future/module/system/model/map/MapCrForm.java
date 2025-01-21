package com.future.module.system.model.map;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@Data
public class MapCrForm {
    @NotBlank(message = "必填")
    private String fullName;
    @NotBlank(message = "必填")
    private String enCode;
    @NotBlank(message = "必填")
    private String data;
    private long sortCode;
    @NotNull(message = "必填")
    private Integer enabledMark;
}
