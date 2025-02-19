package com.future.module.system.model.Template.Template5;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 编辑表格字段
 */
@Data
public class FormControlGridField5Model {
    //列名
    private String colName;
    //字段
    private String field;
    //控件：input、select、date、checkbox、label
    private String control;
    //对齐
    private String align;
    //宽度
    private String width;
}
