package com.future.module.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.future.base.service.SuperService;
import com.future.common.exception.DataException;
import com.future.module.system.entity.InterfaceOauthEntity;
import com.future.module.system.model.InterfaceOauth.PaginationOauth;

import java.util.List;

/**
 * 接口认证服务
 *
 * @author Future Platform Group
 * @version v4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2022/6/8 9:26
 */
public interface InterfaceOauthService extends SuperService<InterfaceOauthEntity> {

    /**
     * 判断接口认证名称是否重复
     *
     * @param appName 名称
     * @param id       主键
     * @return ignore
     */
    boolean isExistByAppName(String appName, String id);

    /**
     * 判断appId是否重复
     *
     * @param appId 名称
     * @param id       主键
     * @return ignore
     */
    boolean isExistByAppId(String appId, String id);


    /**
     * 获取接口列表(分页)
     *
     * @param pagination 分页参数
     * @return ignore
     */
    List<InterfaceOauthEntity> getList(PaginationOauth pagination);

    /**
     * 获取接口认证数据
     *
     * @param id 主键
     * @return ignore
     */
    InterfaceOauthEntity getInfo(String id);

    /**
     * 添加接口认证数据
     *
     * @param entity 实体
     */
    void create(InterfaceOauthEntity entity);
    /**
     * 修改接口
     *
     * @param entity 实体
     * @param id     主键
     * @return 实体
     * @throws DataException ignore
     */
    boolean update(InterfaceOauthEntity entity, String id) throws DataException;

    /**
     * 删除接口
     *
     * @param entity 实体
     */
    void delete(InterfaceOauthEntity entity);

    /**
     * 获取接口认证数据
     *
     * @param appId 主键
     * @return ignore
     */
    InterfaceOauthEntity getInfoByAppId(String appId);
}
