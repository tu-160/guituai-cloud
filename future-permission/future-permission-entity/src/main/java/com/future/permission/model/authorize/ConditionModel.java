package com.future.permission.model.authorize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 数据权限条件
 */
@Data
public class ConditionModel {
    private String logic;
    private List<ConditionItemModel> groups;

    /**
     * 数据权限条件字段
     */
    @Data
    public class ConditionItemModel{
        private String id;
        private String field;
        private String type;
        private String op;
        private String value;
        private String fieldRule;
        private String bindTable;
        private String conditionText;
        private String childTableKey;
    }
}
