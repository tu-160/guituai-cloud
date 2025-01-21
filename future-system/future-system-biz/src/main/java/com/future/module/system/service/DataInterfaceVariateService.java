package com.future.module.system.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.future.base.service.SuperService;
import com.future.common.base.Page;
import com.future.module.system.entity.DataInterfaceVariateEntity;

/**
 * 数据接口业务层
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-03-23
 */
public interface DataInterfaceVariateService extends SuperService<DataInterfaceVariateEntity> {

    /**
     * 列表
     * @param id
     * @param page
     * @return
     */
    List<DataInterfaceVariateEntity> getList(String id, Page page);

    /**
     * 详情
     *
     * @param id
     * @return
     */
    DataInterfaceVariateEntity getInfo(String id);

    /**
     * 判断名称是否重复
     *
     * @param entity
     * @return
     */
    boolean isExistByFullName(DataInterfaceVariateEntity entity);

    /**
     * 添加
     *
     * @param entity
     * @return
     */
    boolean create(DataInterfaceVariateEntity entity);

    /**
     * 修改
     *
     * @param entity
     * @return
     */
    boolean update(DataInterfaceVariateEntity entity);

    /**
     * 删除
     *
     * @param entity
     * @return
     */
    boolean delete(DataInterfaceVariateEntity entity);

    /**
     * 通过id获取列表
     *
     * @param ids
     * @return
     */
    List<DataInterfaceVariateEntity> getListByIds(List<String> ids);

    boolean update(Map<String, String> map, List<DataInterfaceVariateEntity> variateEntities);

    /**
     * 通过名称获取变量
     *
     * @param fullName
     */
    DataInterfaceVariateEntity getInfoByFullName(String fullName);
}
