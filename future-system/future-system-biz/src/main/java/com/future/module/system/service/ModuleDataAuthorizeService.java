package com.future.module.system.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.future.base.service.SuperService;
import com.future.module.system.entity.ModuleDataAuthorizeEntity;

import java.util.List;

/**
 * 数据权限配置
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2019年9月27日 上午9:18
 */
public interface ModuleDataAuthorizeService extends SuperService<ModuleDataAuthorizeEntity> {

    /**
     * 列表
     *
     * @return
     */
    List<ModuleDataAuthorizeEntity> getList();

    /**
     * 列表
     *
     * @param moduleId 功能主键
     * @return
     */
    List<ModuleDataAuthorizeEntity> getList(String moduleId);

    /**
     * 信息
     *
     * @param id 主键值
     * @return
     */
    ModuleDataAuthorizeEntity getInfo(String id);

    /**
     * 创建
     *
     * @param entity 实体对象
     */
    void create(ModuleDataAuthorizeEntity entity);

    /**
     * 更新
     *
     * @param id     主键值
     * @param entity 实体对象
     * @return
     */
    boolean update(String id, ModuleDataAuthorizeEntity entity);

    /**
     * 删除
     *
     * @param entity 实体对象
     */
    void delete(ModuleDataAuthorizeEntity entity);

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

    /**
     * 验证编码是否重复
     *
     * @param moduleId
     * @param enCode
     * @param id
     * @return
     */
    boolean isExistByEnCode(String moduleId, String enCode, String id);

    /**
     * 验证名称是否重复
     *
     * @param moduleId
     * @param fullName
     * @param id
     * @return
     */
    boolean isExistByFullName(String moduleId, String fullName, String id);
}
