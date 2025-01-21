package com.future.module.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.future.base.service.SuperService;
import com.future.common.base.Pagination;
import com.future.module.system.entity.ModuleFormEntity;

import java.util.List;

/**
 *
 * 表单权限
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @author Future Platform Group
* @date 2021-09-14
 */
public interface ModuleFormService extends SuperService<ModuleFormEntity> {

    /**
     * 列表
     *
     * @return
     */
    List<ModuleFormEntity> getList();

    /**
     * 列表
     *
     * @return ignore
     */
    List<ModuleFormEntity> getEnabledMarkList(String enabledMark);

    /**
     * 列表
     *
     * @param moduleId 功能主键
     * @return
     */
    List<ModuleFormEntity> getList(String moduleId, Pagination pagination);

    /**
     * 列表
     *
     * @param moduleId 功能主键
     * @return
     */
    List<ModuleFormEntity> getList(String moduleId);

    /**
     * 信息
     *
     * @param id 主键值
     * @return
     */
    ModuleFormEntity getInfo(String id);

    /**
     * 信息
     *
     * @param id 主键值
     * @param moduleId
     * @return ignore
     */
    ModuleFormEntity getInfo(String id, String moduleId);

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
    void create(ModuleFormEntity entity);

    /**
     * 创建
     * @param entitys 实体对象
     */
    void create(List<ModuleFormEntity> entitys);

    /**
     * 更新
     *
     * @param id     主键值
     * @param entity 实体对象
     */
    boolean update(String id, ModuleFormEntity entity);

    /**
     * 删除
     *
     * @param entity 实体对象
     */
    void delete(ModuleFormEntity entity);

    /**
     * 通过moduleIds获取权限
     *
     * @param ids
     * @return
     */
    List<ModuleFormEntity> getListByModuleId(List<String> ids);

    /**
     * 通过moduleIds获取权限
     *
     * @param ids
     * @return
     */
    List<ModuleFormEntity> getListByIds(List<String> ids);
}
