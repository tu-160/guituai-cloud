package com.future.module.system.fallback;

import org.springframework.stereotype.Component;

import com.future.module.system.AreaApi;
import com.future.module.system.entity.ProvinceEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取行政区划降级处理
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-03-24
 */
@Component
public class AreaApiFallback implements AreaApi {
    @Override
    public List<ProvinceEntity> getList(String id) {
        return null;
    }

    @Override
    public List<ProvinceEntity> getByIdList(List<String> ids) {
        return new ArrayList<>();
    }

    @Override
    public List<ProvinceEntity> getAllProList() {
        return new ArrayList<>();
    }

    @Override
    public List<ProvinceEntity> getProListBytype(String type) {
        return new ArrayList<>();
    }

    @Override
    public ProvinceEntity getProListBytype(String id, List<String> parentId) {
        return null;
    }
}
