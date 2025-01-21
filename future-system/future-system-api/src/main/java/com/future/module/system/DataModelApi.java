package com.future.module.system;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.future.database.model.dbfield.DbFieldModel;
import com.future.database.model.dbfield.base.DbFieldModelBase;
import com.future.database.model.dbtable.DbTableFieldModel;
import com.future.feign.utils.FeignName;
import com.future.module.system.fallback.DataModelApiFallback;

import java.util.List;


/**
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date ：2022/5/6 14:21
 */
@FeignClient(name = FeignName.SYSTEM_SERVER_NAME , fallback = DataModelApiFallback.class, path = "/DataModel")
public interface DataModelApi {

    @PostMapping("/createTable")
    void createTable(@RequestBody List<DbTableFieldModel> dbTable) throws Exception;

    @PostMapping("/addField")
    void addField(@RequestBody DbTableFieldModel dbTable) throws Exception;

    @GetMapping("/getDbTableModel/{linkId}/{table}")
    List<DbFieldModelBase> getDbTableModel(@PathVariable("linkId") String linkId, @PathVariable("table") String table) throws Exception;

    @GetMapping("/getPrimaryDbField/{linkId}/{table}")
    Boolean getPrimaryDbField(@PathVariable("linkId") String linkId, @PathVariable("table") String table) throws Exception;
}
