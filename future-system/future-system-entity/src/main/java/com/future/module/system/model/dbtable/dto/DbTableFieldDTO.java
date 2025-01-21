package com.future.module.system.model.dbtable.dto;

import cn.hutool.core.util.ObjectUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import com.future.common.exception.DataException;
import com.future.database.constant.DbAliasConst;
import com.future.database.model.dbfield.DbFieldModel;
import com.future.database.model.dbtable.DbTableFieldModel;
import com.future.module.system.model.dbtable.form.DbFieldForm;
import com.future.module.system.model.dbtable.form.DbTableForm;

/**
 * 建表参数对象
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021/3/12 15:31
 */
@Data
public class DbTableFieldDTO {

    @Schema(description = "表基本信息")
    private DbTableForm tableInfo;

    @Schema(description = "字段信息")
    private List<DbFieldForm> tableFieldList;

    /**
     * 获取表模型
     *
     * @param dbLinkId 数据库连接Id
     * @return 表模型
     */
    public DbTableFieldModel getCreDbTableModel(String dbLinkId){
        String table = this.getTableInfo().getNewTable();
        return toCommon(table, null, null, dbLinkId);
    }

    public DbTableFieldModel getUpDbTableModel(String dbLinkId){
        // 更新时用表
        String oldTable = this.getTableInfo().getTable();
        String newTable = this.getTableInfo().getNewTable();
        return toCommon(null, oldTable, newTable, dbLinkId);
    }

    private DbTableFieldModel toCommon(String table, String oldTable, String newTable, String dbLinkId) throws DataException {
        DbTableFieldModel dbTableModel = new DbTableFieldModel();
        DbTableForm dbTableForm = this.getTableInfo();
        // 数据连接Id
        dbTableModel.setDbLinkId(dbLinkId);
        // 创建表名
        dbTableModel.setTable(table);
        // 更新时表名
        dbTableModel.setUpdateOldTable(oldTable);
        dbTableModel.setUpdateNewTable(newTable);
        // 表注释
        dbTableModel.setComment(dbTableForm.getTableName());
        // 表字段集合
        List<DbFieldModel> list = new ArrayList<>();
        for (DbFieldForm dbFieldForm : this.getTableFieldList()) {
            // 字段
            DbFieldModel dbFieldModel = new DbFieldModel();
            // 字段名
            dbFieldModel.setField(dbFieldForm.getField());
            // 字段注释
            dbFieldModel.setComment(dbFieldForm.getFieldName());
            // 主键
            dbFieldModel.setIsPrimaryKey(dbFieldForm.getPrimaryKey() == 1);
            // 非空
            dbFieldModel.setNullSign(DbAliasConst.ALLOW_NULL.getSign(dbFieldForm.getAllowNull()));
            // 数据类型
            dbFieldModel.setLength(dbFieldForm.getDataLength());
            dbFieldModel.setDataType(dbFieldForm.getDataType());
            dbFieldModel.setIsAutoIncrement(ObjectUtil.equal(dbFieldForm.getAutoIncrement(), 1));
            if (dbFieldModel.getIsAutoIncrement() && (!"int".equals(dbFieldForm.getDataType()) && !"bigint".equals(dbFieldForm.getDataType()))) {
                throw new DataException("自增长ID字段数据类型必须为整形或长整型");
            }
            list.add(dbFieldModel);
        }
        dbTableModel.setDbFieldModelList(list);
        return dbTableModel;
    }

}
