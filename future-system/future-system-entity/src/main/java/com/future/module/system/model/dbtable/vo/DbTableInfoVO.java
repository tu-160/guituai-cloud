package com.future.module.system.model.dbtable.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import com.future.database.model.dbfield.DbFieldModel;
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
public class DbTableInfoVO {

    @Schema(description ="表信息")
    private DbTableVO tableInfo;
    @Schema(description ="字段信息集合")
    private List<DbFieldVO> tableFieldList;
    @Schema(description ="表是否存在信息")
    private Boolean hasTableData;



    public DbTableInfoVO(DbTableFieldModel dbTableModel, List<DbFieldModel> dbFieldModelList){
        if(dbTableModel != null){
            List<DbFieldVO> list = new ArrayList<>();
            for (DbFieldModel dbFieldModel : dbFieldModelList) {
                list.add(new DbFieldVO(dbFieldModel));
            }
            this.tableFieldList = list;
            this.tableInfo = new DbTableVO(dbTableModel);
        }
    }

}
