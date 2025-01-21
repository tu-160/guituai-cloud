package com.future.module.system.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.future.base.service.SuperService;
import com.future.module.system.entity.ComFieldsEntity;

import java.util.List;

/**
 *
 * 常用字段表
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-03-23
 */
public interface ComFieldsService extends SuperService<ComFieldsEntity> {

    List<ComFieldsEntity> getList();

    ComFieldsEntity getInfo(String id);

    void create(ComFieldsEntity entity);

    boolean update(String id, ComFieldsEntity entity);

    /**
     * 验证名称
     *
     * @param fullName 名称
     * @param id       主键值
     * @return
     */
    boolean isExistByFullName(String fullName, String id);

    void delete(ComFieldsEntity entity);
}
