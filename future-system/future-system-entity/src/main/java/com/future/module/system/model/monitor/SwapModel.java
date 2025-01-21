package com.future.module.system.model.monitor;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SwapModel {
    private String total;
    private String available;
    private String used;
    private String usageRate;
}
