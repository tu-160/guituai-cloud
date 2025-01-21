package com.future.module.system.model.Template.Template5;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 子表列表
 */
@Data
public class IndexGridEntry5Model {
    //标题
    private String title;
    //表名
    private String table;
    //类的名称（添加的字段）
    private String className;
    //查询的主键(添加的字段)
    private DbTableRelation5Model dbTableRelation;
    // 列表字段
    private List<IndexGridField5Model> gridTableFieldList;
}
