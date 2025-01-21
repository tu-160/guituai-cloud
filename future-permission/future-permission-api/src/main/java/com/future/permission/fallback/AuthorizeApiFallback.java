package com.future.permission.fallback;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.future.module.system.model.base.SystemBaeModel;
import com.future.module.system.model.button.ButtonModel;
import com.future.module.system.model.column.ColumnModel;
import com.future.module.system.model.form.ModuleFormModel;
import com.future.module.system.model.module.ModuleModel;
import com.future.module.system.model.resource.ResourceModel;
import com.future.permission.AuthorizeApi;
import com.future.permission.entity.AuthorizeEntity;
import com.future.permission.model.authorize.AuthorizeConditionModel;
import com.future.permission.model.authorize.AuthorizeVO;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 获取权限信息Api降级处理
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-03-24
 */
@Component
public class AuthorizeApiFallback implements AuthorizeApi {

    @Override
    public List<AuthorizeEntity> getListByObjectId(String objectId) {
        return null;
    }

    @Override
    public List<AuthorizeEntity> getListByObjectId(String objectId, String type) {
        return new ArrayList<>();
    }

    @Override
    public void remove(QueryWrapper<AuthorizeEntity> queryWrapper) {

    }

    @Override
    public AuthorizeVO getAuthorize(boolean isCache, boolean singletonOrg) {
        List<ModuleModel> moduleList = new ArrayList<>();
        List<ButtonModel> buttonList = new ArrayList<>();
        List<ColumnModel> columnList = new ArrayList<>();
        List<ResourceModel> resourceList = new ArrayList<>();
        List<ModuleFormModel> formsList = new ArrayList<>();
        List<SystemBaeModel> systemBaeModels = new ArrayList<>();
        return new AuthorizeVO(moduleList, buttonList, columnList, resourceList, formsList, systemBaeModels);
    }

    @Override
    public byte[] getConditionSql(AuthorizeConditionModel conditionModel) {
        return null;
    }

    @Override
    public byte[] getCondition(AuthorizeConditionModel conditionModel) {
        return null;
    }

//    @Override
//    public List<SystemBaeModel> findSystem(List<String> roleIds) {
//        return new ArrayList<>();
//    }

    @Override
    public List<ButtonModel> findButton(String objectId) {
        return null;
    }

    @Override
    public List<ColumnModel> findColumn(String objectId) {
        return null;
    }

    @Override
    public List<ResourceModel> findResource(String objectId) {
        return null;
    }

    @Override
    public List<ModuleFormModel> findForms(String objectId) {
        return null;
    }

    @Override
    public List<ButtonModel> findButtonAdmin(Integer mark) {
        return null;
    }

    @Override
    public List<ColumnModel> findColumnAdmin(Integer mark) {
        return null;
    }

    @Override
    public List<ResourceModel> findResourceAdmin(Integer mark) {
        return null;
    }

    @Override
    public List<ModuleFormModel> findFormsAdmin(Integer mark) {
        return null;
    }

    @Override
    public List<AuthorizeEntity> getAuthorizeByItem(String itemType, String itemId) {
        return Collections.EMPTY_LIST;
    }

    @Override
    public AuthorizeVO getAuthorizeByUser(boolean singletonOrg) {
        return new AuthorizeVO(new ArrayList<>(), new ArrayList<>(),new ArrayList<>(),new ArrayList<>(),new ArrayList<>(),new ArrayList<>());
    }
}
