package com.future.permission.fallback;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import com.future.permission.PermissionGroupApi;
import com.future.permission.entity.PermissionGroupEntity;

import java.util.Collections;
import java.util.List;

@Component
public class PermissionGroupApiFallback implements PermissionGroupApi {
    @Override
    public List<PermissionGroupEntity> getPermissionGroupByUserId(String userId) {
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<PermissionGroupEntity> getPermissionGroupByUserIdAndTenantId(String userId, String tenantId, @RequestParam(value = "systemId", required = false) String systemId) {
        return Collections.EMPTY_LIST;
    }

    @Override
    public String getOrganizeIdByUserIdAndTenantId(String userId, String tenantId) {
        return "";
    }

    @Override
    public List<PermissionGroupEntity> getPermissionGroupByModuleId(String moduleId) {
        return Collections.EMPTY_LIST;
    }

    @Override
    public PermissionGroupEntity getInfoById(String id) {
        return null;
    }

    @Override
    public List<PermissionGroupEntity> getListByIds(List<String> ids) {
        return Collections.EMPTY_LIST;
    }
}
