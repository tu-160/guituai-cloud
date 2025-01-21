package com.future.provider.system;

import java.util.List;

import com.future.module.system.entity.DictionaryDataEntity;

/**
 * 数据字典服务提供
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-05-06
 */
public interface DictionaryDataProvider {

    /**
     * 列表
     *
     * @return
     */
    List<DictionaryDataEntity> getList();

    /**
     * 列表
     *
     * @param dictionaryTypeId 类别主键
     * @return
     */
    List<DictionaryDataEntity> getList(String dictionaryTypeId);

    /**
     * 获取名称
     * @param id
     * @return
     */
    List<DictionaryDataEntity> getDictionName(List<String> id);
}
