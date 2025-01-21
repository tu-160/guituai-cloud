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
 * 使用用户ID获取用户信息
 */
@Service(AuthConsts.USERDETAIL_USER_ID)
public class UserDetailsByUserIdServiceImpl implements UserDetailService {

    private static final Integer ORDER = 1;

    @Autowired
    private UserApi userApi;

    @Override
    public UserEntity loadUserEntity(UserInfo userInfo) throws LoginException {
        UserEntity userEntity = userApi.getInfoByUserId(new UserInfoModel(userInfo.getUserId(), userInfo.getTenantId()));
        if (userEntity == null) {
            throw new LoginException(MsgCode.LOG101.get());
        }
        return userEntity;
    }

    @Override
    public int getOrder() {
        return ORDER;
    }

}
