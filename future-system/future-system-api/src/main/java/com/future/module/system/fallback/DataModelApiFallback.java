package com.future.module.system.fallback;

import org.springframework.stereotype.Component;

import com.future.common.exception.DataException;
import com.future.database.model.dbfield.DbFieldModel;
import com.future.database.model.dbfield.base.DbFieldModelBase;
import com.future.database.model.dbtable.DbTableFieldModel;
import com.future.module.system.DataModelApi;

import java.util.ArrayList;
import java.util.List;


/**
 *
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-03-24
 */
@Component
public class DataModelApiFallback implements DataModelApi {

    @Override
    public void createTable(List<DbTableFieldModel> dbTable) throws DataException {
        throw new DataException("创建失败");
    }

    @Override
    public void addField(DbTableFieldModel dbTable) throws DataException {
        throw new DataException("创建失败");
    }

    @Override
    public List<DbFieldModelBase> getDbTableModel(String linkId, String table) throws Exception {
        return new ArrayList<>();
    }

    @Override
    public Boolean getPrimaryDbField(String linkId, String table) throws Exception {
        return null;
    }
}
