package com.future.module.oauth.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.future.common.base.UserInfo;
import com.future.common.constant.MsgCode;
import com.future.common.consts.AuthConsts;
import com.future.common.exception.LoginException;
import com.future.common.service.UserDetailService;
import com.future.permission.UserApi;
import com.future.permission.entity.UserEntity;
import com.future.permission.model.user.UserInfoModel;


/**
 * 默认使用用户名获取用户信息
 */
@Service(AuthConsts.USERDETAIL_ACCOUNT)
public class UserDetailsByUserAccountServiceImpl implements UserDetailService {

    @Autowired
    private UserApi userApi;

    @Override
    public UserEntity loadUserEntity(UserInfo userInfo) throws LoginException {
        UserEntity userEntity = userApi.getInfoByAccount(new UserInfoModel(userInfo.getUserAccount(), userInfo.getTenantId()));
        if (userEntity == null) {
            throw new LoginException(MsgCode.LOG101.get());
        }
        return userEntity;
    }


    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }

}
