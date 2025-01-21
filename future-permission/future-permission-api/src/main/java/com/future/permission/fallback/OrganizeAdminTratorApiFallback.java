package com.future.permission.fallback;

import org.springframework.stereotype.Component;

import com.future.permission.OrganizeAdminTratorApi;
import com.future.permission.entity.OrganizeAdministratorEntity;
import com.future.permission.model.organizeadministrator.OrganizeAdministratorModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 获取组织信息Api降级处理
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-03-24
 */
@Component
public class OrganizeAdminTratorApiFallback implements OrganizeAdminTratorApi {

    @Override
    public List<OrganizeAdministratorEntity> getListByUserId(String userId, String type) {
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<String> getOrganizeUserList(String type) {
        return Collections.EMPTY_LIST;
    }

    @Override
    public OrganizeAdministratorModel getOrganizeAdministratorList() {
        return new OrganizeAdministratorModel();
    }

    @Override
    public boolean saveOrganizeAdminTrator(OrganizeAdministratorEntity entity) {
        return false;
    }

    @Override
    public List<OrganizeAdministratorEntity> getInfoByUserId(String userId, String tenantId) {
        return new ArrayList<>();
    }
}
