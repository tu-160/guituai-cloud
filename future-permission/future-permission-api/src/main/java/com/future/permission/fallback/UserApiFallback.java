package com.future.permission.fallback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import com.future.common.base.ActionResult;
import com.future.common.exception.DataException;
import com.future.permission.UserApi;
import com.future.permission.entity.UserEntity;
import com.future.permission.model.user.*;

import java.util.*;

/**
 * 获取用户信息Api降级处理
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-03-24
 */
@Component
@Slf4j
public class UserApiFallback implements UserApi {

    @Override
    public List<UserEntity> getUserName(List<String> id) {
        return new ArrayList<>();
    }

    @Override
    public List<UserEntity> getUserNamePagination(UserModel userModel) {
        return new ArrayList<>();
    }

    @Override
    public ActionResult<UserInfoVO> getInfo(String id)  throws DataException {
        return null;
    }

    @Override
    public List<String> getListId() {
        return new ArrayList<>();
    }

    @Override
    public UserEntity getInfoByAccount(UserInfoModel userInfoModel) {
        return null;
    }

    @Override
    public UserEntity getInfoByUserId(UserInfoModel userInfoModel) {
        return null;
    }

    @Override
    public Boolean updateById(UserUpdateModel userUpdateModel) {
        return false;
    }

    @Override
    public Boolean update(String id, UserEntity entity) {return false; }

    @Override
    public UserEntity getInfoByAccount(String account) {
        return null;
    }

    @Override
    public Boolean getAccountIsExist(String account) {
        return null;
    }

    @Override
    public UserEntity getInfoById(String id) {
        return null;
    }

    @Override
    public UserEntity getInfoByIdInMessage(String id) {
        return new UserEntity();
    }

    @Override
    public List<UserEntity> getListByManagerId(String id) {
        return new ArrayList<>();
    }

    @Override
    public List<UserEntity> getList(boolean enabledMark) {
        return new ArrayList<>();
    }

    @Override
    public List<UserEntity> getAdminList() {
        return new ArrayList<>();
    }

    @Override
    public Boolean setAdminListByIds(List<String> adminIds) {
        return false;
    }

    @Override
    public UserEntity getByRealName(String fullName) {
        return null;
    }

    @Override
    public Boolean updateUserById(UserEntity userEntity) {
        return false;
    }

    @Override
    public Map<String, Object> getUserMap(String type) {
        return new HashMap<>();
    }

    @Override
    public UserEntity getInfoByMobile(String mobile) {
        return null;
    }

    @Override
    public Boolean create(UserEntity userEntity) {return false; }

    @Override
    public void delete(UserEntity userEntity) {

    }

    @Override
    public List<UserByRoleVO> getUserByRoleList(String organizeId) {
        return new ArrayList<>();
    }

    @Override
    public List<String> getUserIdList(List<String> userIds) {
        return new ArrayList<>();
    }

    @Override
    public String getDefaultCurrentUserId(@RequestBody UserConditionModel userConditionModel) { return ""; }

    @Override
    public List<UserIdListVo> selectedByIds(List<String> ids) {
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<UserEntity> getUserNameMark(List<String> id, boolean filterEnabledMark) {
        return null;
    }
}
