package com.future.module.system.fallback;

import org.springframework.stereotype.Component;

import com.future.module.system.SynThirdInfoApi;
import com.future.module.system.entity.SynThirdInfoEntity;
import com.future.permission.model.SynOrganizeDeleteModel;
import com.future.permission.model.SynOrganizeModel;
import com.future.permission.model.SynThirdQyModel;
import com.future.permission.model.SysThirdDeleteModel;

/**
 * 第三方工具同步表模型
 *
 * @@version V4.0.0
 * @@copyright 直方信息科技有限公司
 * @@author Future Platform Group
 * @date 2021/4/25 9:21
 */
@Component
public class SynThirdInfoApiFallback implements SynThirdInfoApi {

    @Override
    public SynThirdInfoEntity getInfoBySysObjId(String thirdType, String dataType, String id, String tenantId) {
        return null;
    }

    @Override
    public void createUserSysToQy(SynThirdQyModel synThirdQyModel) {
    }

    @Override
    public void createUserSysToDing(SynThirdQyModel synThirdQyModel) {
    }

    @Override
    public void updateUserSysToQy(SynThirdQyModel synThirdQyModel) {

    }

    @Override
    public void updateUserSysToDing(SynThirdQyModel synThirdQyModel) {

    }

    @Override
    public void deleteUserSysToQy(SysThirdDeleteModel sysThirdDeleteModel) {

    }

    @Override
    public void deleteUserSysToDing(SysThirdDeleteModel sysThirdDeleteModel) {

    }

    @Override
    public void createDepartmentSysToQy(SynOrganizeModel synOrganizeModel) {

    }

    @Override
    public void createDepartmentSysToDing(SynOrganizeModel synOrganizeModel) {

    }

    @Override
    public void updateDepartmentSysToQy(SynOrganizeModel synOrganizeModel) {

    }

    @Override
    public void updateDepartmentSysToDing(SynOrganizeModel synOrganizeModel) {

    }

    @Override
    public void deleteDepartmentSysToQy(SynOrganizeDeleteModel synOrganizeDeleteModel) {

    }

    @Override
    public void deleteDepartmentSysToDing(SynOrganizeDeleteModel synOrganizeDeleteModel) {

    }
}
