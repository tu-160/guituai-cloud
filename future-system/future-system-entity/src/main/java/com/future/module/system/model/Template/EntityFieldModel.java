package com.future.module.system.model.Template;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 实体字段模型
 */
@Data
public class EntityFieldModel {
    // 字段名称
    private String Field;
    // 字段说明
    private String FieldName;
    // 数据类型
    private String DataType;
}
