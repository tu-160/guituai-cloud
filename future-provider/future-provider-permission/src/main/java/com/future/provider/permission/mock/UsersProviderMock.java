package com.future.provider.permission.mock;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

import com.future.permission.entity.UserEntity;
import com.future.permission.model.user.UserAllModel;
import com.future.provider.permission.UsersProvider;

/**
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-05-11
 */
@Slf4j
public class UsersProviderMock implements UsersProvider {
    @Override
    public UserEntity getUserByAccount(String account) {
        log.error("获取用户信息接口调用失败");
        return null;
    }

    @Override
    public List<UserAllModel> getAll() {
        return null;
    }

    @Override
    public UserEntity getInfo(String id) {
        return null;
    }

    @Override
    public List<UserEntity> getList() {
        return null;
    }

    @Override
    public boolean update(String id, UserEntity entity) {
        return false;
    }
}
