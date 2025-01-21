package com.future.module.system.model.dbtable.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

import com.future.database.model.dbtable.DbTableFieldModel;

/**
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021/3/12 15:31
 */
@Data
@NoArgsConstructor
public class DbTableVO {

    @NotBlank(message = "必填")
    @Schema(description ="表名")
    private String table;
    @NotBlank(message = "必填")
    @Schema(description ="表注释")
    private String tableName;

    public DbTableVO(DbTableFieldModel dbTableFieldModel){
        this.table = dbTableFieldModel.getTable();
        this.tableName = dbTableFieldModel.getComment();
    }

}
