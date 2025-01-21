package com.future.module.system.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.future.base.service.SuperService;
import com.future.common.base.Pagination;
import com.future.module.system.entity.ModuleButtonEntity;

import java.util.List;

/**
 * 按钮权限
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2019年9月27日 上午9:18
 */
public interface ModuleButtonService extends SuperService<ModuleButtonEntity> {

    /**
     * 列表
     *
     * @return
     */
    List<ModuleButtonEntity> getListByModuleIds();

    /**
     * 列表
     *
     * @return ignore
     */
    List<ModuleButtonEntity> getEnabledMarkList(String enabledMark);

    /**
     * 列表
     *
     * @param moduleId 功能主键
     * @return
     */
    List<ModuleButtonEntity> getListByModuleIds(String moduleId);

    /**
     * 列表(带关键字的)
     *
     * @param moduleId 功能主键
     * @param pagination
     * @return
     */
    List<ModuleButtonEntity> getListByModuleIds(String moduleId, Pagination pagination);

    /**
     * 信息
     *
     * @param id 主键值
     * @return
     */
    ModuleButtonEntity getInfo(String id);

    /**
     * 信息
     *
     * @param id 主键值
     * @param moduleId
     * @return ignore
     */
    ModuleButtonEntity getInfo(String id, String moduleId);

    /**
     * 验证名称
     *
     * @param moduleId 功能主键
     * @param fullName 名称
     * @param id       主键值
     * @return
     */
    boolean isExistByFullName(String moduleId, String fullName, String id);

    /**
     * 验证编码
     *
     * @param moduleId 功能主键
     * @param enCode   编码
     * @param id       主键值
     * @return
     */
    boolean isExistByEnCode(String moduleId, String enCode, String id);

    /**
     * 创建
     *
     * @param entity 实体对象
     */
    void create(ModuleButtonEntity entity);

    /**
     * 更新
     *
     * @param id     主键值
     * @param entity 实体对象
     * @return
     */
    boolean update(String id, ModuleButtonEntity entity);

    /**
     * 删除
     *
     * @param entity 实体对象
     */
    void delete(ModuleButtonEntity entity);

    /**
     * 通过moduleIds获取按钮权限
     *
     * @param ids
     * @return
     */
    List<ModuleButtonEntity> getListByModuleIds(List<String> ids);

    /**
     * 通过moduleIds获取按钮权限
     *
     * @param ids
     * @return
     */
    List<ModuleButtonEntity> getListByIds(List<String> ids);
}
