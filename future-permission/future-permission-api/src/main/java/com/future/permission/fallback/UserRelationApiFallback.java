package com.future.permission.fallback;

import org.springframework.stereotype.Component;

import com.future.permission.UserRelationApi;
import com.future.permission.entity.UserRelationEntity;
import com.future.permission.model.authorize.AuthorizeListModel;
import com.future.permission.model.permission.PermissionModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取用户关系Api降级处理
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-03-24
 */
@Component
public class UserRelationApiFallback implements UserRelationApi {
    @Override
    public List<UserRelationEntity> getList(String userId) {
        return new ArrayList<>();
    }

    @Override
    public List<UserRelationEntity> getList(String userId, String objectType) {
        return new ArrayList<>();
    }

    @Override
    public List<UserRelationEntity> getListByUserIdAll(List<String> id) {
        return new ArrayList<>();
    }

    @Override
    public List<UserRelationEntity> getListByObjectIdAll(List<String> id) {
        return new ArrayList<>();
    }

    @Override
    public List<PermissionModel> getObjectVoList(String objectType) {
        return new ArrayList<>();
    }


}
