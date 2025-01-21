package com.future.module.system.model.dbsync;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.Map;

import com.future.database.model.dbtable.DbTableFieldModel;

/**
 * 类功能
 *
 * @author Future Platform Group
 * @version v4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2022-06-01
 */
@Data
public class DbSyncVo {

    /**
     * 验证结果
     */
    @Schema(description = "验证结果")
    private Boolean checkDbFlag;

    /**
     * 表集合
     */
    @Schema(description = "表集合")
    private List<DbTableFieldModel> tableList;

    /**
     * 转换规则
     */
    @Schema(description = "转换规则")
    private Map<String, List<String>> convertRuleMap;

}
