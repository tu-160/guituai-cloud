package com.future.provider.permission;

import java.util.List;

import com.future.permission.entity.UserEntity;
import com.future.permission.model.user.UserAllModel;

/**
 * 使用RPC获取用户
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-04-29
 */
public interface UsersProvider {

    /**
     * 通过account返回user实体
     *
     * @param account 账户
     * @return
     */
    UserEntity getUserByAccount(String account);

    /**
     * 获取所有用户信息
     * @return
     */
    List<UserAllModel> getAll();

    /**
     * 信息
     *
     * @param id 主键值
     * @return
     */
    UserEntity getInfo(String id);

    /**
     * 列表
     *
     * @return
     */
    List<UserEntity> getList();

    /**
     * 更新
     *
     * @param id     主键值
     * @param entity 实体对象
     */
    boolean update(String id, UserEntity entity);

}
