package com.future.permission.fallback;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import com.future.permission.RoleApi;
import com.future.permission.entity.RoleEntity;
import com.future.permission.model.role.RoleInfoModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date ：2022/4/7 9:06
 */
@Component
public class RoleApiFallback implements RoleApi {
    @Override
    public List<RoleEntity> getListByUserId(RoleInfoModel roleInfoModel) {
        return new ArrayList<RoleEntity>(16);
    }

    @Override
    public List<RoleEntity> getByUserId(String userId) {
        return null;
    }

    @Override
    public RoleEntity getInfoById(String id) {
        return null;
    }

    @Override
    public List<String> getRoleIdsByCurrentUser() {
        return new ArrayList<>();
    }

    @Override
    public List<RoleEntity> getListByIds(List<String> roleIds) {
        return new ArrayList<>();
    }

    @Override
    public List<String> getAllRoleIdsByUserIdAndOrgId(String userId, String orgId) {
        return new ArrayList<>();
    }

    @Override
    public List<RoleEntity> getListAll() {
        return new ArrayList<>();
    }

    @Override
    public Map<String, Object> getRoleMap(String type) {
        return new HashMap<>();
    }
}
