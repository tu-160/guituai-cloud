package com.future.module.system.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.future.base.service.SuperService;
import com.future.common.base.ActionResult;
import com.future.common.base.vo.DownloadVO;
import com.future.common.exception.DataException;
import com.future.module.system.entity.DictionaryDataEntity;
import com.future.module.system.model.dictionarydata.DictionaryExportModel;

import java.util.List;

/**
 * 字典数据
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2019年9月27日 上午9:18
 */
public interface DictionaryDataService extends SuperService<DictionaryDataEntity> {

    /**
     * 列表
     *
     * @return
     */
    List<DictionaryDataEntity> getList(String dictionaryTypeId, Boolean enable);

    /**
     * 列表
     *
     * @param dictionaryTypeId 类别主键
     * @return
     */
    List<DictionaryDataEntity> getList(String dictionaryTypeId);

    /**
     * 列表
     *
     * @param dictionaryTypeId 类别主键(在线开发数据转换)
     * @return ignore
     */
    List<DictionaryDataEntity> getDicList(String dictionaryTypeId);
    /**
     * 列表
     *
     * @param parentId 父级id
     * @return ignore
     */
    Boolean isExistSubset(String parentId);


    /**
     * 代码生成器数据字典转换
     * @param value encode 或者 id
     * @return
     */
    DictionaryDataEntity getSwapInfo(String value,String parentId);

    /**
     * 信息
     *
     * @param id 主键值
     * @return
     */
    DictionaryDataEntity getInfo(String id);

    /**
     * 验证名称
     *
     * @param dictionaryTypeId 类别主键
     * @param fullName         名称
     * @param id               主键值
     * @return
     */
    boolean isExistByFullName(String dictionaryTypeId, String fullName, String id);

    /**
     * 验证编码
     *
     * @param dictionaryTypeId 类别主键
     * @param enCode           编码
     * @param id               主键值
     * @return
     */
    boolean isExistByEnCode(String dictionaryTypeId, String enCode, String id);

    /**
     * 删除
     *
     * @param entity 实体对象
     */
    void delete(DictionaryDataEntity entity);

    /**
     * 创建
     *
     * @param entity 实体对象
     */
    void create(DictionaryDataEntity entity);

    /**
     * 更新
     * @param id        主键值
     * @param entity    实体对象
     * @return
     */
    boolean update(String id, DictionaryDataEntity entity);

    /**
     * 上移
     * @param id    主键值
     * @return
     */
    boolean first(String id);

    /**
     * 下移
     * @param id    主键值
     * @return
     */
    boolean next(String id);

    /**
     * 获取名称
     * @param id
     * @return
     */
    List<DictionaryDataEntity> getDictionName(List<String> id);

    /**
     * 通过数据字典分类和字典数据的encode 获取数据信息
     * @param typeCode  分类code
     * @param dataCode  数据code
     * @return
     */
    DictionaryDataEntity getByTypeDataCode(String typeCode,String dataCode);

    /**
     * 导出数据
     * @param   id
     * @return  DownloadVO
     */
    DownloadVO exportData(String id);

    /**
     * 导入数据
     * @param exportModel
     * @return
     */
    ActionResult importData(DictionaryExportModel exportModel, Integer type) throws DataException;

    List<DictionaryDataEntity> getListByTypeDataCode(String typeCode);
}
