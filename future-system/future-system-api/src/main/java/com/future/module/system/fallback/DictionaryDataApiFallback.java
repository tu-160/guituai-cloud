package com.future.module.system.fallback;

import org.springframework.stereotype.Component;

import com.future.common.base.ActionResult;
import com.future.common.exception.DataException;
import com.future.module.system.DictionaryDataApi;
import com.future.module.system.entity.DictionaryDataEntity;

import java.util.ArrayList;
import java.util.List;
/**
 * 调用数据字典Api降级处理
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-03-24
 */
@Component
public class DictionaryDataApiFallback implements DictionaryDataApi {
    @Override
    public List<DictionaryDataEntity> getList(String dictionary) {
        return new ArrayList<>();
    }

    @Override
    public DictionaryDataEntity getInfo(String id) {
        return null;
    }

    @Override
    public ActionResult getListByTypeDataCode(String typeCode) {
        return ActionResult.success(new ArrayList<>());
    }

    @Override
    public List<DictionaryDataEntity> getListByCode(String typeCode) {
        return new ArrayList<>();
    }

    @Override
    public ActionResult<DictionaryDataEntity> getByTypeDataCode(String typeCode, String dataCode) throws DataException {
        return null;
    }

    @Override
    public List<DictionaryDataEntity> getDicList(String dictionaryTypeId) {
        return new ArrayList<>();
    }

    @Override
    public List<DictionaryDataEntity> getList(String dictionaryTypeId, String enable) {
        return new ArrayList<>();
    }

    @Override
    public List<DictionaryDataEntity> getDictionName(List<String> id) {
        return new ArrayList<>();
    }

    @Override
    public DictionaryDataEntity getSwapInfo(String value, String parentId) {
        return null;
    }

}
