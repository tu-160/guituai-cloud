package com.future.module.system.model.dbtable.form;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 表信息表单信息
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021/3/12 15:31
 */
@Data
public class DbTableForm  {

    @Schema(description ="表名")
    private String table;

    @NotBlank(message = "必填")
    @Schema(description ="表说明")
    private String tableName;

    @Schema(description ="新表名")
    private String newTable;

}
