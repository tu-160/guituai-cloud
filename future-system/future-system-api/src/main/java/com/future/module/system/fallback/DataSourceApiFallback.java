package com.future.module.system.fallback;

import org.springframework.stereotype.Component;

import com.future.database.model.entity.DbLinkEntity;
import com.future.module.system.DataSourceApi;

/**
 * 调用数据接口Api降级处理
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-03-24
 */
@Component
public class DataSourceApiFallback implements DataSourceApi {


    @Override
    public DbLinkEntity getInfo(String id) {
        return null;
    }

    @Override
    public Object getInfo(String id, String tenantId) {
        return null;
    }

    @Override
    public DbLinkEntity getInfoByFullName(String fullName) {
        return null;
    }

    @Override
    public DbLinkEntity getResource(String dbLinkId, String tenantId) throws Exception {
        return new DbLinkEntity();
    }

}
