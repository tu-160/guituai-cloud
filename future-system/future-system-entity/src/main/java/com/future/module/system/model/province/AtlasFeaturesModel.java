package com.future.module.system.model.province;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AtlasFeaturesModel {
    private String type;
    private AtlasPropModel properties;
    private Object geometry;
}
