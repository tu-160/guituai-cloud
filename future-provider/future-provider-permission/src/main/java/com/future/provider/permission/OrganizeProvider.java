package com.future.provider.permission;

import java.util.List;

import com.future.permission.entity.OrganizeEntity;

/**
 * 使用RPC调用组织
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-04-29
 */
public interface OrganizeProvider {

    /**
     * 通过Id获取组织信息
     * @param id
     * @return
     */
    OrganizeEntity organizeById(String id);

    /**
     * 列表
     *
     * @return
     */
    List<OrganizeEntity> getList();

}
