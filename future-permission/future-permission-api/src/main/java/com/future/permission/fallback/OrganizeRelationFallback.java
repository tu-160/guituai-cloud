package com.future.permission.fallback;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import com.future.permission.OrganizeRelationApi;
import com.future.permission.entity.OrganizeRelationEntity;
import com.future.permission.entity.PermissionGroupEntity;
import com.future.permission.model.organize.OrganizeConditionModel;
import com.future.permission.model.organize.OrganizeModel;
import com.future.permission.model.organizerelation.AutoGetMajorOrgIdModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date ：2022/4/7 10:30
 */
@Component
public class OrganizeRelationFallback implements OrganizeRelationApi {
    @Override
    public Boolean existByRoleIdAndOrgId(String roleId, String orgId) {
        return false;
    }

    @Override
    public List<PermissionGroupEntity> checkBasePermission(String userId, String orgId, @RequestParam(value = "orgId", required = false) String systemId) {
        return new ArrayList<>();
    }

    @Override
    public String autoGetMajorOrganizeId(AutoGetMajorOrgIdModel autoGetMajorOrgIdModel) {
        return "";
    }

    @Override
    public String autoGetMajorPositionId(String userId, String organizeId, String positionId) {
        return "";
    }

    @Override
    public List<OrganizeRelationEntity> getRelationListByOrganizeId(List<String> ableIds, String type) {
        return new ArrayList();
    }

    @Override
    public List<String> getOrgIds(List<String> departIds) {
        return new ArrayList();
    }

    @Override
    public List<OrganizeModel> getOrgIdsList(OrganizeConditionModel organizeConditionModel) {
        return new ArrayList();
    }
}
