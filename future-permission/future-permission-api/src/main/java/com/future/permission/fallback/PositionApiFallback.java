package com.future.permission.fallback;

import org.springframework.beans.PropertyValues;
import org.springframework.stereotype.Component;

import com.future.common.exception.DataException;
import com.future.permission.PositionApi;
import com.future.permission.entity.PositionEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 获取岗位信息Api降级处理
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-03-24
 */
@Component
public class PositionApiFallback implements PositionApi {

    @Override
    public PositionEntity queryInfoById(String id) {
        return null;
    }

    @Override
    public List<PositionEntity> getPositionName(List<String> posiList, Boolean filterEnabledMark) {
        return new ArrayList<>();
    }

    @Override
    public PositionEntity getByFullName(String fullName) {
        return null;
    }

    @Override
    public Map<String, Object> getPosMap(String type) {
        return new HashMap<>();
    }

    @Override
    public List<PositionEntity> getListByOrganizeId(List<String> ableDepIds) {
        return new ArrayList<>();
    }

}
