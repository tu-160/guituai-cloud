package com.future.module.system.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.future.base.service.SuperService;
import com.future.common.base.Page;
import com.future.module.system.entity.DictionaryTypeEntity;

import java.util.List;

/**
 * 字典分类
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2019年9月27日 上午9:18
 */
public interface DictionaryTypeService extends SuperService<DictionaryTypeEntity> {

    /**
     * 列表
     *
     * @return
     */
    List<DictionaryTypeEntity> getList();

    /**
     * 列表
     * @param page
     * @return
     */
    List<DictionaryTypeEntity> getList(Page page, Integer category);

    /**
     * 信息
     *
     * @param enCode 代码
     * @return
     */
    DictionaryTypeEntity getInfoByEnCode(String enCode);

    /**
     * 信息
     *
     * @param id 主键值
     * @return
     */
    DictionaryTypeEntity getInfo(String id);

    /**
     * 验证名称
     *
     * @param fullName 名称
     * @param id       主键值
     * @return
     */
    boolean isExistByFullName(String fullName, String id);

    /**
     * 验证编码
     *
     * @param enCode 编码
     * @param id     主键值
     * @return
     */
    boolean isExistByEnCode(String enCode, String id);

    /**
     * 创建
     *
     * @param entity 实体对象
     */
    void create(DictionaryTypeEntity entity);

    /**
     * 更新
     *
     * @param id     主键值
     * @param entity 实体对象
     * @return
     */
    boolean update(String id, DictionaryTypeEntity entity);

    /**
     * 删除
     *
     * @param entity 实体对象
     * @return
     */
    boolean delete(DictionaryTypeEntity entity);

    /**
     * 上移
     *
     * @param id 主键值
     * @return
     */
    boolean first(String id);

    /**
     * 下移
     *
     * @param id 主键值
     * @return
     */
    boolean next(String id);
}
