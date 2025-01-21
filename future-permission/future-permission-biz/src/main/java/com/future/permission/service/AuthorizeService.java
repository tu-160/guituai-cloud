package com.future.permission.service;

import java.util.List;

import com.future.base.service.SuperService;
import com.future.common.base.UserInfo;
import com.future.module.system.entity.SystemEntity;
import com.future.module.system.model.button.ButtonModel;
import com.future.module.system.model.column.ColumnModel;
import com.future.module.system.model.form.ModuleFormModel;
import com.future.module.system.model.resource.ResourceModel;
import com.future.permission.entity.AuthorizeEntity;
import com.future.permission.model.authorize.*;
import com.future.visualdev.portal.model.PortalModel;

/**
 * 操作权限
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2019年9月26日 上午9:18
 */
public interface AuthorizeService extends SuperService<AuthorizeEntity> {

    /**
     * 获取权限（菜单、按钮、列表）
     *
     * @param userInfo 对象
     * @param singletonOrg
     * @return
     */
    AuthorizeVO getAuthorize(UserInfo userInfo, boolean singletonOrg) throws Exception;

    /**
     * 获取权限（菜单、按钮、列表）
     *
     * @param isCache 是否存在redis
     * @param singletonOrg
     * @return
     */
    AuthorizeVO getAuthorize(boolean isCache, boolean singletonOrg);

    /**
     * 创建
     *
     * @param objectId      对象主键
     * @param authorizeList 实体对象
     */
    void save(String objectId, AuthorizeDataUpForm authorizeList);

    /**
     * 创建
     *
     * @param saveBatchForm 对象主键
     */
    void saveBatch(SaveBatchForm saveBatchForm, boolean isBatch);

    /**
     * 根据用户id获取列表
     *
     * @param isAdmin 是否管理员
     * @param userId  用户主键
     * @return
     */
    List<AuthorizeEntity> getListByUserId(boolean isAdmin, String userId);

    /**
     * 根据对象Id获取列表
     *
     * @param objectId 对象主键
     * @return
     */
    List<AuthorizeEntity> getListByObjectId(List<String> objectId);

    /**
     * 判断当前角色是否有权限
     *
     * @param roleId
     * @param systemId
     * @return
     */
    Boolean existAuthorize(String roleId, String systemId);

    /**
     * 判断当前角色是否有权限
     *
     * @param roleId
     * @return
     */
    List<AuthorizeEntity> getListByRoleId(String roleId);

    /**
     * 根据对象Id获取列表
     *
     * @param objectId 对象主键
     * @param itemType 对象主键
     * @return
     */
    List<AuthorizeEntity> getListByObjectId(String objectId, String itemType);

    /**
     * 根据对象Id获取列表
     *
     * @param objectType 对象主键
     * @return
     */
    List<AuthorizeEntity> getListByObjectAndItem(String itemId, String objectType);

    /**
     * 根据对象Id获取列表
     *
     * @param itemId 对象主键
     * @param itemType 对象类型
     * @return
     */
    List<AuthorizeEntity> getListByObjectAndItemIdAndType(String itemId, String itemType);

    void savePortalManage(String portalManageId, SaveAuthForm saveAuthForm);

    void getPortal(List<SystemEntity> systemList, List<PortalModel> portalList, Long dateTime, List<String> collect);

    void savePortalAuth(String permissionGroupId, List<String> portalIds);

    byte[] getCondition(AuthorizeConditionModel conditionModel);


    List<ButtonModel> findButton(String objectId);

    List<ColumnModel> findColumn(String objectId);

    List<ResourceModel> findResource(String objectId);

    List<ModuleFormModel> findForms(String objectId);

    List<ButtonModel> findButtonAdmin(Integer mark);

    List<ColumnModel> findColumnAdmin(Integer mark);

    List<ResourceModel> findResourceAdmin(Integer mark);

    List<ModuleFormModel> findFormsAdmin(Integer mark);

    /**
     * 通过Item获取权限列表
     *
     * @param itemType
     * @param itemId
     * @return
     */
    List<AuthorizeEntity> getAuthorizeByItem(String itemType, String itemId);

    AuthorizeVO getAuthorizeByUser(boolean singletonOrg);

    AuthorizeVO getMainSystemAuthorize(List<String> moduleIds, List<String> moduleAuthorize, List<String> moduleUrlAddressAuthorize, boolean singletonOrg);

    List<AuthorizeEntity> getListByRoleIdsAndItemType(List<String> roleIds, String itemType);
}
