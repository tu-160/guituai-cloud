package com.future.provider.permission;

import com.future.permission.entity.RoleEntity;

/**
 * 使用RPC获取角色
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-04-29
 */
public interface RoleProvider {
    /**
     * 信息
     *
     * @param id 主键值
     * @return
     */
    RoleEntity getInfo(String id);
}
