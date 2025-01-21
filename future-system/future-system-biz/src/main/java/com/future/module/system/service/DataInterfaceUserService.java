package com.future.module.system.service;

import java.util.List;

import com.future.base.service.SuperService;
import com.future.module.system.entity.DataInterfaceUserEntity;
import com.future.module.system.model.InterfaceOauth.InterfaceUserForm;

/**
 * @author Future Platform Group
 * @version v4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021/9/20 9:22
 */
public interface DataInterfaceUserService extends SuperService<DataInterfaceUserEntity> {

    /**
     * 授权用户
     *
     * @param interfaceUserForm
     */
    void saveUserList(InterfaceUserForm interfaceUserForm);

    /**
     * 根据认证接口id查询授权用户列表
     *
     * @param oauthId
     * @return
     */
    List<DataInterfaceUserEntity> select(String oauthId);

    /**
     * 通过用户密钥获取用户token
     *
     * @param oauthId
     * @param userKey
     * @return
     */
    String getInterfaceUserToken(String tenantId, String oauthId, String userKey);

}
