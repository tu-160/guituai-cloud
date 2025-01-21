package com.future.provider.permission;

import java.util.List;

import com.future.permission.entity.UserRelationEntity;

/**
 * 使用RPC获取用户关系
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-04-29
 */
public interface UserRelationProvider {
    /**
     * 根据用户主键获取列表
     *
     * @param userId 用户主键
     * @return
     */
    List<UserRelationEntity> getListByUserId(String userId);
}
