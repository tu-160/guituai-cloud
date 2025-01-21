package com.future.permission.service.impl;


import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.future.base.service.SuperServiceImpl;
import com.future.common.base.*;
import com.future.common.base.entity.*;
import com.future.common.constant.PlatformConst;
import com.future.common.constant.PermissionConst;
import com.future.common.emnus.SearchMethodEnum;
import com.future.common.model.tenant.TenantAuthorizeModel;
import com.future.common.util.*;
import com.future.database.model.dto.PrepSqlDTO;
import com.future.database.sql.util.SqlFrameFastUtil;
import com.future.database.util.DataSourceUtil;
import com.future.database.util.DbTypeUtil;
import com.future.database.util.JdbcUtil;
import com.future.database.util.TenantDataSourceUtil;
import com.future.module.system.ModuleApi;
import com.future.module.system.ModuleButtonApi;
import com.future.module.system.ModuleColumnApi;
import com.future.module.system.ModuleDataAuthorizeSchemeApi;
import com.future.module.system.ModuleFormApi;
import com.future.module.system.PortalManageApi;
import com.future.module.system.SystemApi;
import com.future.module.system.entity.ModuleButtonEntity;
import com.future.module.system.entity.ModuleColumnEntity;
import com.future.module.system.entity.ModuleDataAuthorizeSchemeEntity;
import com.future.module.system.entity.ModuleEntity;
import com.future.module.system.entity.ModuleFormEntity;
import com.future.module.system.entity.SystemEntity;
import com.future.module.system.model.base.SystemApiByIdsModel;
import com.future.module.system.model.base.SystemApiModel;
import com.future.module.system.model.base.SystemBaeModel;
import com.future.module.system.model.button.ButtonModel;
import com.future.module.system.model.column.ColumnModel;
import com.future.module.system.model.form.ModuleFormModel;
import com.future.module.system.model.module.ModuleApiByIdAndMarkModel;
import com.future.module.system.model.module.ModuleApiByIdsModel;
import com.future.module.system.model.module.ModuleApiModel;
import com.future.module.system.model.module.ModuleModel;
import com.future.module.system.model.portalManage.PortalManagePageDO;
import com.future.module.system.model.portalManage.PortalManageSelectModel;
import com.future.module.system.model.resource.ResourceModel;
import com.future.permission.constant.AuthorizeConst;
import com.future.permission.entity.*;
import com.future.permission.mapper.AuthorizeMapper;
import com.future.permission.model.authorize.*;
import com.future.permission.model.portalManage.AuthorizePortalManagePrimary;
import com.future.permission.service.*;
import com.future.reids.config.ConfigValueUtil;
import com.future.reids.util.RedisUtil;
import com.future.visualdev.portal.model.PortalModel;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 操作权限
 *
 * @author Future Platform GroupgetAuthorize
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2019年9月26日 上午9:18
 */
@Slf4j
@Service
public class AuthorizeServiceImpl extends SuperServiceImpl<AuthorizeMapper, AuthorizeEntity> implements AuthorizeService {

    @Autowired
    private UserRelationService userRelationService;
    @Autowired
    private DataSourceUtil dataSourceUtils;
    @Autowired
    private UserProvider userProvider;
    @Autowired
    private CacheKeyUtil cacheKeyUtil;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private OrganizeService organizeService;
    @Autowired
    private OrganizeAdministratorService organizeAdminIsTratorService;
    @Autowired
    private ConfigValueUtil configValueUtil;
    @Autowired
    private SystemApi systemApi;
    @Autowired
    private PermissionGroupService permissionGroupService;
    @Autowired
    private OrganizeAdministratorService organizeAdministratorService;
    @Autowired
    private ModuleButtonApi buttonApi;
    @Autowired
    private ModuleColumnApi columnApi;
    @Autowired
    private ModuleFormApi formApi;
    @Autowired
    private ModuleDataAuthorizeSchemeApi dataAuthorizeSchemeApi;
    @Autowired
    private ModuleApi moduleApi;
    @Autowired
    private PortalManageApi portalManageApi;

    @Override
    public AuthorizeVO getAuthorize(boolean isCache, boolean singletonOrg) {
        // 是否从缓冲里面获取权限
        if (isCache) {
            return getCacheAuthor(userProvider.get());
        } else {
            return getAuthorize(userProvider.get(), singletonOrg);
        }
    }

    private AuthorizeVO getCacheAuthor(UserInfo userInfo) {
        // 是否从缓冲里面获取权限
        String cacheKey = cacheKeyUtil.getUserAuthorize() + userInfo.getUserId();
        if (!redisUtil.exists(cacheKey)) {
            AuthorizeVO authorizeModel = getAuthorize(userInfo, false);
            if (authorizeModel.getModuleList().size() != 0) {
                redisUtil.insert(cacheKey, authorizeModel, 60);
            }
            return authorizeModel;
        } else {
            return JsonUtil.getJsonToBean(redisUtil.getString(cacheKey).toString(), AuthorizeVO.class);
        }
    }


    @Override
    public AuthorizeVO getAuthorize(UserInfo userInfo, boolean singletonOrg) {
        List<ModuleModel> moduleList = new ArrayList<>();
        List<ButtonModel> buttonList = new ArrayList<>();
        List<ColumnModel> columnList = new ArrayList<>();
        List<ResourceModel> resourceList = new ArrayList<>();
        List<ModuleFormModel> formsList = new ArrayList<>();
        List<SystemBaeModel> systemList = new ArrayList<>();
        Boolean isAdmin = userInfo.getIsAdministrator();

        SystemEntity entity = systemApi.getInfoByEnCode(PlatformConst.MAIN_SYSTEM_CODE);
        List<String> moduleAuthorize = new ArrayList<>();
        List<String> moduleUrlAddressAuthorize = new ArrayList<>();
        if (configValueUtil.isMultiTenancy()) {
            TenantAuthorizeModel tenantAuthorizeModel = TenantDataSourceUtil.getCacheModuleAuthorize(userInfo.getTenantId());
            moduleAuthorize = tenantAuthorizeModel.getModuleIdList();
            moduleUrlAddressAuthorize = tenantAuthorizeModel.getUrlAddressList();
        }
        if (!isAdmin) {
            List<String> roleIds = new ArrayList<>();
//            roleService.getRoleIdsByCurrentUser(userInfo.getOrganizeId()).forEach(role -> {
//                RoleEntity info = roleService.getInfo(role);
//                //判断角色状态是否为有效，显示当前组织内角色的并集
//                if (info != null && info.getEnabledMark() == 1) {
//                    roleIds.add("'" + role + "'");
//                }
//            });
            // 应用 无开发平台
            List<OrganizeAdministratorEntity> organizeAdministratorEntity = organizeAdminIsTratorService.getInfoByUserId(userInfo.getUserId(), null);
            if (organizeAdministratorEntity.size() > 0) {
                List<OrganizeAdministratorEntity> listByUserId = organizeAdminIsTratorService.getOrganizeAdministratorEntity(userInfo.getUserId(), PermissionConst.SYSTEM, true);
                List<SystemEntity> systemEntities = systemApi.getListByIds(new SystemApiByIdsModel(listByUserId.stream().map(OrganizeAdministratorEntity::getOrganizeId).collect(Collectors.toList()), moduleAuthorize));
                List<SystemBaeModel> systemJsonToList = JsonUtil.getJsonToList(systemEntities, SystemBaeModel.class);
                systemList.addAll(systemJsonToList);
                List<String> systemIds = systemJsonToList.stream().map(SystemBaeModel::getId).collect(Collectors.toList());
                List<ModuleEntity> moduleBySystemIds = moduleApi.getModuleBySystemIds(new ModuleApiByIdsModel(systemIds, moduleAuthorize, moduleUrlAddressAuthorize, false));
                List<ModuleModel> moduleJsonToList = JsonUtil.getJsonToList(moduleBySystemIds, ModuleModel.class);
                moduleList.addAll(moduleJsonToList);
                List<ModuleButtonEntity> buttonByModuleId = buttonApi.getListByModuleIds(moduleJsonToList.stream().map(ModuleModel::getId).collect(Collectors.toList()));
                List<ButtonModel> buttonJsonToList = JsonUtil.getJsonToList(buttonByModuleId, ButtonModel.class);
                buttonList.addAll(buttonJsonToList);
                List<ModuleColumnEntity> columnByModuleId = columnApi.getListByModuleId(moduleJsonToList.stream().map(ModuleModel::getId).collect(Collectors.toList()));
                List<ColumnModel> columnJsonToList = JsonUtil.getJsonToList(columnByModuleId, ColumnModel.class);
                columnList.addAll(columnJsonToList);
                List<ModuleDataAuthorizeSchemeEntity> resourceByModuleId = dataAuthorizeSchemeApi.getListByModuleId(moduleJsonToList.stream().map(ModuleModel::getId).collect(Collectors.toList()));
                List<ResourceModel> resourceJsonToList = JsonUtil.getJsonToList(resourceByModuleId, ResourceModel.class);
                resourceList.addAll(resourceJsonToList);
                List<ModuleFormEntity> formByModuleId = formApi.getListByModuleId(moduleJsonToList.stream().map(ModuleModel::getId).collect(Collectors.toList()));
                List<ModuleFormModel> formJsonToList = JsonUtil.getJsonToList(formByModuleId, ModuleFormModel.class);
                formsList.addAll(formJsonToList);
            } else {
                List<String> roleIdList = new ArrayList<>();
                permissionGroupService.getPermissionGroupByUserId(userInfo.getUserId(), userInfo.getOrganizeId(), true, null).forEach(t -> {
                    roleIdList.add(t.getId());
                    roleIds.add("'" + t.getId() + "'");
                });
                if (roleIds.size() > 0) {
                    String roleIdsStr = String.join(",", roleIds);
                    List<AuthorizeEntity> listByRoleIdsAndItemType = getListByRoleIdsAndItemType(roleIdList, AuthorizeConst.SYSTEM);
                    List<SystemEntity> systemAdmin = systemApi.getListByIds(new SystemApiByIdsModel(listByRoleIdsAndItemType.stream().map(AuthorizeEntity::getItemId).collect(Collectors.toList()), moduleAuthorize));
                    systemList = JsonUtil.getJsonToList(systemAdmin, SystemBaeModel.class);
                    systemList = systemList.stream().distinct().collect(Collectors.toList());
                    // 菜单 无开发平台
                    moduleList = this.baseMapper.findModule(roleIdsStr, entity.getId(), moduleAuthorize, moduleUrlAddressAuthorize, 1);
                    moduleList = moduleList.stream().distinct().collect(Collectors.toList());
                    // 按钮
                    buttonList = this.baseMapper.findButton(roleIdsStr);
                    buttonList = buttonList.stream().distinct().collect(Collectors.toList());
                    // 列表
                    columnList = this.baseMapper.findColumn(roleIdsStr);
                    columnList = columnList.stream().distinct().collect(Collectors.toList());
                    // 数据
                    resourceList = this.baseMapper.findResource(roleIdsStr);
                    resourceList = resourceList.stream().distinct().collect(Collectors.toList());
                    // 表单
                    formsList = this.baseMapper.findForms(roleIdsStr);
                    formsList = formsList.stream().distinct().collect(Collectors.toList());
                }
            }

        } else {
            List<ModuleEntity> moduleAdmin = moduleApi.findModuleAdmin(new ModuleApiByIdAndMarkModel(singletonOrg ? 0 : 1, entity.getId(), moduleAuthorize, moduleUrlAddressAuthorize));
            moduleList = JsonUtil.getJsonToList(moduleAdmin, ModuleModel.class);
            buttonList = this.baseMapper.findButtonAdmin(1);
            columnList = this.baseMapper.findColumnAdmin(1);
            resourceList = this.baseMapper.findResourceAdmin(1);
            formsList = this.baseMapper.findFormsAdmin(1);
            List<SystemEntity> systemAdmin = systemApi.findSystemAdmin(new SystemApiModel(singletonOrg ? 0 : 1, PlatformConst.MAIN_SYSTEM_CODE, moduleAuthorize));
            systemList = JsonUtil.getJsonToList(systemAdmin, SystemBaeModel.class);
        }
        return new AuthorizeVO(moduleList, buttonList, columnList, resourceList, formsList, systemList);
    }

    @Override
    public AuthorizeVO getMainSystemAuthorize(List<String> moduleIds, List<String> moduleAuthorize, List<String> moduleUrlAddressAuthorize, boolean singletonOrg) {
        SystemEntity systemEntity = systemApi.getInfoByEnCode(PlatformConst.MAIN_SYSTEM_CODE);
        if (systemEntity == null || moduleAuthorize.contains(systemEntity.getId())) {
            return new AuthorizeVO();
        }
        SystemBaeModel systemBaeModel = JsonUtil.getJsonToBean(systemEntity, SystemBaeModel.class);
        List<ModuleEntity> moduleList = new ArrayList<>();
        if (moduleIds.size() > 0) {
            moduleList = moduleApi.getModuleByIds(new ModuleApiByIdsModel(moduleIds, moduleAuthorize, moduleUrlAddressAuthorize, singletonOrg));
        } else {
            moduleList = moduleApi.getMainModule(ModuleApiModel.builder().moduleAuthorize(moduleAuthorize).moduleUrlAddressAuthorize(moduleUrlAddressAuthorize).singletonOrg(singletonOrg).build());
        }
        List<ModuleModel> moduleModelList = JsonUtil.getJsonToList(moduleList, ModuleModel.class);
        List<ModuleButtonEntity> buttonByModuleId = buttonApi.getListByModuleIds(moduleModelList.stream().map(ModuleModel::getId).collect(Collectors.toList()));
        List<ButtonModel> buttonJsonToList = JsonUtil.getJsonToList(buttonByModuleId, ButtonModel.class);

        List<ModuleColumnEntity> columnByModuleId = columnApi.getListByModuleId(moduleModelList.stream().map(ModuleModel::getId).collect(Collectors.toList()));
        List<ColumnModel> columnJsonToList = JsonUtil.getJsonToList(columnByModuleId, ColumnModel.class);

        List<ModuleDataAuthorizeSchemeEntity> resourceByModuleId = dataAuthorizeSchemeApi.getListByModuleId(moduleModelList.stream().map(ModuleModel::getId).collect(Collectors.toList()));
        List<ResourceModel> resourceJsonToList = JsonUtil.getJsonToList(resourceByModuleId, ResourceModel.class);

        List<ModuleFormEntity> formByModuleId = formApi.getListByModuleId(moduleModelList.stream().map(ModuleModel::getId).collect(Collectors.toList()));
        List<ModuleFormModel> formJsonToList = JsonUtil.getJsonToList(formByModuleId, ModuleFormModel.class);

        return new AuthorizeVO(moduleModelList, buttonJsonToList, columnJsonToList, resourceJsonToList, formJsonToList, Collections.singletonList(systemBaeModel));
    }

    @Override
    public List<AuthorizeEntity> getListByRoleIdsAndItemType(List<String> roleIds, String itemType) {
        if (roleIds.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        QueryWrapper<AuthorizeEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(AuthorizeEntity::getItemType, itemType);
        queryWrapper.lambda().in(AuthorizeEntity::getObjectId, roleIds);
        return this.list(queryWrapper);
    }

    @Override
    public void save(String objectId, AuthorizeDataUpForm authorizeList) {
        SaveBatchForm form = JsonUtil.getJsonToBean(authorizeList, SaveBatchForm.class);
        form.setRoleIds(new String[]{objectId});
        this.saveBatch(form, false);
    }

    @Override
    public void saveBatch(SaveBatchForm saveBatchForm, boolean isBatch) {
        try {
            UserInfo userInfo = userProvider.get();

            List<AuthorizeEntity> objectList = new ArrayList<>();
            List<AuthorizeEntity> authorizeList = new ArrayList<>();
            // 设置权限归属对象
            setEntity(saveBatchForm.getUserIds(), AuthorizeConst.USER, objectList, true);
            setEntity(saveBatchForm.getPositionIds(), AuthorizeConst.POSITION, objectList, true);
            setEntity(saveBatchForm.getRoleIds(), AuthorizeConst.ROLE, objectList, true);
//            setEntity(saveBatchForm.getSystemIds(), AuthorizeConst.SYSTEM, objectList, true);
            // 设置权限模块
            setEntity(saveBatchForm.getButton(), AuthorizeConst.BUTTON, authorizeList, false);
            setEntity(saveBatchForm.getModule(), AuthorizeConst.MODULE, authorizeList, false);
            setEntity(saveBatchForm.getColumn(), AuthorizeConst.COLUMN, authorizeList, false);
            setEntity(saveBatchForm.getResource(), AuthorizeConst.RESOURCE, authorizeList, false);
            setEntity(saveBatchForm.getForm(), AuthorizeConst.FROM, authorizeList, false);
            setEntity(saveBatchForm.getSystemIds(), AuthorizeConst.SYSTEM, authorizeList, false);

            // 删除角色相关信息
            List<String> objectIdAll = objectList.stream().map(AuthorizeEntity::getObjectId).collect(Collectors.toList());
            userService.delCurRoleUser(objectIdAll);
            if (!isBatch) {
                String ids = String.join(",", objectIdAll);
                JdbcUtil.creUpDe(new PrepSqlDTO(XSSEscape.escapeEmpty(SqlFrameFastUtil.AUTHOR_DEL.replace("{authorizeIds}", ids))).withConn(dataSourceUtils, null));
            }

            // 插入数据
            String sql = DbTypeUtil.checkOracle(dataSourceUtils) || DbTypeUtil.checkPostgre(dataSourceUtils) ?
                    SqlFrameFastUtil.INSERT_AUTHORIZE2 : SqlFrameFastUtil.INSERT_AUTHORIZE;

            String column_key = StringUtil.EMPTY, column_plceholder = StringUtil.EMPTY, column_value = TenantDataSourceUtil.getTenantColumn();
            if (StringUtil.isNotEmpty(column_value)) {
                column_key = StrPool.COMMA + configValueUtil.getMultiTenantColumn();
                column_plceholder = ",?";
            }
            sql = sql.replace("%COLUMN_KEY%", column_key).replace("%COLUMN_PLACEHOLDER%", column_plceholder);
            PrepSqlDTO dto = new PrepSqlDTO(sql).withConn(dataSourceUtils, null);
            for (int i = 0; i < objectList.size(); i++) {
                for (AuthorizeEntity entityItem : authorizeList) {
                    List<Object> data = new LinkedList<>();
                    data.add(RandomUtil.uuId());
                    data.add(entityItem.getItemType());
                    data.add(entityItem.getItemId());
                    data.add(objectList.get(i).getObjectType());
                    data.add(objectList.get(i).getObjectId());
                    data.add(i);
                    data.add(DateUtil.getNow());
                    data.add(userInfo.getUserId());
                    if (StringUtil.isNotEmpty(column_value)) {
                        data.add(column_value);
                    }
                    dto.addMultiData(data);
                }
            }
            JdbcUtil.creUpDeBatchOneSql(dto);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("权限报错:" + e.getMessage());
        }
    }

    /**
     * 权限
     */
    private void setEntity(String[] ids, String type, List<AuthorizeEntity> entityList, Boolean objectFlag) {
        if (ids != null) {
            for (String id : ids) {
                AuthorizeEntity entity = new AuthorizeEntity();
                if (objectFlag) {
                    entity.setObjectType(type);
                    entity.setObjectId(id);
                } else {
                    entity.setItemType(type);
                    entity.setItemId(id);
                }
                entityList.add(entity);
            }
        }
    }

    @Override
    public List<AuthorizeEntity> getListByUserId(boolean isAdmin, String userId) {
        if (!isAdmin) {
            QueryWrapper<UserRelationEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(UserRelationEntity::getUserId, userId);
            List<UserRelationEntity> list = userRelationService.list(queryWrapper);
            List<String> userRelationList = list.stream().map(u -> u.getObjectId()).collect(Collectors.toList());
            userRelationList.add(userId);
            QueryWrapper<AuthorizeEntity> wrapper = new QueryWrapper<>();
            wrapper.lambda().in(AuthorizeEntity::getObjectId, userRelationList);
            return this.list(wrapper);
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<AuthorizeEntity> getListByObjectId(List<String> objectId) {
        if (objectId.size() == 0) {
            return new ArrayList<>();
        }
        QueryWrapper<AuthorizeEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(AuthorizeEntity::getObjectId, objectId);
        return this.list(queryWrapper);
    }

    @Override
    public Boolean existAuthorize(String roleId, String systemId) {
        QueryWrapper<AuthorizeEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(AuthorizeEntity::getObjectId, roleId);
        if (StringUtil.isNotEmpty(systemId)) {
            queryWrapper.lambda().eq(AuthorizeEntity::getItemId, systemId);
            queryWrapper.lambda().eq(AuthorizeEntity::getItemType, AuthorizeConst.SYSTEM);
        }
        return this.count(queryWrapper) > 0;
    }

    @Override
    public List<AuthorizeEntity> getListByRoleId(String roleId) {
        QueryWrapper<AuthorizeEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(AuthorizeEntity::getObjectId, roleId);
        return this.list(queryWrapper);
    }

    @Override
    public List<AuthorizeEntity> getListByObjectId(String objectId, String itemType) {
        QueryWrapper<AuthorizeEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(AuthorizeEntity::getObjectId, objectId);
        queryWrapper.lambda().eq(AuthorizeEntity::getItemType, itemType);
        return this.list(queryWrapper);
    }

    @Override
    public List<AuthorizeEntity> getListByObjectAndItem(String itemId, String objectType) {
        QueryWrapper<AuthorizeEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(AuthorizeEntity::getObjectType, objectType).eq(AuthorizeEntity::getItemId, itemId);
        return this.list(queryWrapper);
    }

    @Override
    public List<AuthorizeEntity> getListByObjectAndItemIdAndType(String itemId, String itemType) {
        QueryWrapper<AuthorizeEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(AuthorizeEntity::getItemType, itemType).eq(AuthorizeEntity::getItemId, itemId);
        return this.list(queryWrapper);
    }

    @Override
    public void savePortalManage(String portalManageId, SaveAuthForm saveAuthForm) {
        final String AUTHORIZE_ROLE = "role";
        String userId = UserProvider.getLoginUserId();
        // 原始授权角色
        List<AuthorizeEntity> list = new ArrayList<>();
        for (int i = 0; i < saveAuthForm.getObjectId().length; i++) {
            AuthorizeEntity authorizeEntity = new AuthorizeEntity();
            authorizeEntity.setId(RandomUtil.uuId());
            authorizeEntity.setItemType(AuthorizeConst.AUTHORIZE_PORTAL_MANAGE);
            authorizeEntity.setItemId(portalManageId);
            authorizeEntity.setObjectType(AUTHORIZE_ROLE);
            authorizeEntity.setObjectId(saveAuthForm.getObjectId()[i]);
            authorizeEntity.setSortCode((long) i);
            authorizeEntity.setCreatorTime(new Date());
            authorizeEntity.setCreatorUserId(userId);
            list.add(authorizeEntity);
        }
        remove(new AuthorizePortalManagePrimary(null, portalManageId).getQuery());
        saveBatch(list);
    }

    @Override
    public void getPortal(List<SystemEntity> systemList, List<PortalModel> portalList, Long dateTime, List<String> collect) {
        Map<String, SystemEntity> systemBaeModelMap = systemList.stream().collect(Collectors.toMap(SystemEntity::getId, Function.identity()));
        List<String> systemIds = systemList.stream().map(SystemEntity::getId).collect(Collectors.toList());
        List<PortalManagePageDO> portalManagePageDOS = portalManageApi.selectPortalBySystemIds(new PortalManageSelectModel(systemIds, collect));
        if (portalManagePageDOS.size() == 0) {
            return;
        }
        Map<String, List<PortalManagePageDO>> systemIdAndPortalMap = portalManagePageDOS.stream().collect(Collectors.groupingBy(PortalManagePageDO::getSystemId));
        if (systemIdAndPortalMap != null) {
            systemIdAndPortalMap.keySet().forEach(t -> {
                if (Optional.ofNullable(systemBaeModelMap.get(t)).isPresent()) {
                    PortalModel systemModel = JsonUtil.getJsonToBean(systemBaeModelMap.get(t), PortalModel.class);
                    systemModel.setParentId("-1");
                    portalList.add(systemModel);
                    Map<String, String> platFormId = new HashMap<>();
                    List<PortalManagePageDO> portalManagePageDOList = systemIdAndPortalMap.get(t);
                    Map<String, List<PortalManagePageDO>> platFormMap = portalManagePageDOList.stream().collect(Collectors.groupingBy(PortalManagePageDO::getPlatform));
                    List<PortalManagePageDO> web = platFormMap.get("Web");
                    List<PortalManagePageDO> app = platFormMap.get("App");
                    if (web != null && web.size() > 0) {
                        PortalModel platForm = new PortalModel();
                        platForm.setId(systemModel.getId() + "1");
                        platForm.setParentId(systemModel.getId());
                        platForm.setFullName("WEB门户");
                        platForm.setIcon("icon-ym icon-ym-pc");
                        platForm.setSortCode(0L);
                        platForm.setCreatorTime(dateTime);
                        platFormId.put("Web", platForm.getId());
                        portalList.add(platForm);
                    }
                    if (app != null && app.size() > 0) {
                        PortalModel platForm = new PortalModel();
                        platForm.setId(systemModel.getId() + "2");
                        platForm.setParentId(systemModel.getId());
                        platForm.setFullName("APP门户");
                        platForm.setIcon("icon-ym icon-ym-mobile");
                        platForm.setSortCode(0L);
                        platForm.setCreatorTime(dateTime);
                        platFormId.put("App", platForm.getId());
                        portalList.add(platForm);
                    }
                    portalManagePageDOList.forEach(pageDO -> {
//                        if (!categoryList.contains(pageDO.getCategoryId())) {
//                            categoryList.add(pageDO.getCategoryId());
//                            PortalModel categoryModel = new PortalModel();
//                            categoryModel.setId(pageDO.getCategoryId());
//                            categoryModel.setParentId(platFormId.get(pageDO.getPlatform()));
//                            categoryModel.setFullName(pageDO.getCategoryName());
//                            categoryModel.setOnlyId(RandomUtil.uuId());
//                            list.add(categoryModel);
//                        }
                        PortalModel model = JsonUtil.getJsonToBean(pageDO, PortalModel.class);
//                        model.setParentId(pageDO.getCategoryId());
                        model.setParentId(platFormId.get(pageDO.getPlatform()));
                        model.setFullName(pageDO.getPortalName());
                        portalList.add(model);
                    });
                }
            });
        }
    }

    @Override
    @Transactional
    public void savePortalAuth(String permissionGroupId, List<String> portalIds) {
        final String AUTHORIZE_ROLE = "role";
        String userId = UserProvider.getLoginUserId();
        // 原始授权角色
        List<AuthorizeEntity> list = new ArrayList<>();
        for (int i = 0; i < portalIds.size(); i++) {
            AuthorizeEntity authorizeEntity = new AuthorizeEntity();
            authorizeEntity.setId(RandomUtil.uuId());
            authorizeEntity.setItemType(AuthorizeConst.AUTHORIZE_PORTAL_MANAGE);
            authorizeEntity.setItemId(portalIds.get(i));
            authorizeEntity.setObjectType(AUTHORIZE_ROLE);
            authorizeEntity.setObjectId(permissionGroupId);
            authorizeEntity.setSortCode((long) i);
            authorizeEntity.setCreatorTime(new Date());
            authorizeEntity.setCreatorUserId(userId);
            list.add(authorizeEntity);
        }
        QueryWrapper<AuthorizeEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(AuthorizeEntity::getItemType, AuthorizeConst.AUTHORIZE_PORTAL_MANAGE);
        queryWrapper.lambda().eq(AuthorizeEntity::getObjectId, permissionGroupId);
        this.remove(queryWrapper);
        list.forEach(this::save);
    }

    /**
     * 获取条件过滤
     *
     * @param conditionModel 数据权限条件模型
     */
    @Override
    @DS("")
    public byte[] getCondition(AuthorizeConditionModel conditionModel) {
        QueryWrapper<?> queryWhere = new QueryWrapper<>();
        String moduleId = conditionModel.getModuleId();
        String tableName = conditionModel.getTableName();
        UserInfo userInfo = userProvider.get();
        AuthorizeVO model = this.getAuthorize(true, false);
        List<ResourceModel> resourceList = model.getResourceList().stream().filter(m -> m.getModuleId().equals(moduleId)).collect(Collectors.toList());
        if (resourceList.size() == 0) {
            return null;
        }
        List<ResourceModel> resourceList1 = new ArrayList<>();
        //拼接计数
        int t = 1;
        for (ResourceModel item : resourceList) {
            if (item.getAllData() != null && item.getAllData() == 1) {
                t = 0;
                break;
            }
            List<ConditionModel> conditionModelList = JsonUtil.getJsonToList(item.getConditionJson(), ConditionModel.class);
            for (int i = 0; i < conditionModelList.size(); i++) {
                ConditionModel conditionItem = conditionModelList.get(i);
                for (int k = 0; k < conditionItem.getGroups().size(); k++) {
                    ConditionModel.ConditionItemModel fieldItem = conditionItem.getGroups().get(k);
                    String itemTable = fieldItem.getBindTable();
                    if (StringUtil.isNotEmpty(itemTable) && itemTable.equalsIgnoreCase(tableName)) {
                        resourceList1.add(item);
                    }
                }
            }
        }
        if (t == 1) {
            if (resourceList1.size() > 0) {
                queryWhere.and(tw -> {
                    for (ResourceModel item : resourceList1) {
                        List<ConditionModel> conditionModelList = JsonUtil.getJsonToList(item.getConditionJson(), ConditionModel.class);
                        for (int i = 0; i < conditionModelList.size(); i++) {
                            ConditionModel conditionItem = conditionModelList.get(i);
                            for (int k = 0; k < conditionItem.getGroups().size(); k++) {
                                ConditionModel.ConditionItemModel fieldItem = conditionItem.getGroups().get(k);
                                String itemField = fieldItem.getField();
                                String itemValue = fieldItem.getValue();
                                String itemMethod = fieldItem.getOp();
                                Object value;
                                if (AuthorizeConditionEnum.USER.getCondition().equals(itemValue)) { //当前用户
                                    value = userInfo.getUserId();
                                    //任意文本 当前用户 当前组织 包含为模糊查询
                                    if (itemMethod.equals(SearchMethodEnum.Included.getMessage())) {
                                        itemMethod = SearchMethodEnum.Like.getMessage();
                                    }
                                    if (itemMethod.equals(SearchMethodEnum.NotIncluded.getMessage())) {
                                        itemMethod = SearchMethodEnum.NotLike.getMessage();
                                    }
                                } else if (AuthorizeConditionEnum.ORGANIZE.getCondition().equals(itemValue)) { //当前组织
                                    String orgId = userInfo.getOrganizeId();
                                    if (StringUtil.isNotEmpty(userInfo.getDepartmentId())) {
                                        orgId = userInfo.getDepartmentId();
                                    }
                                    if (itemMethod.equals(SearchMethodEnum.Included.getMessage())) {
                                        itemMethod = SearchMethodEnum.Like.getMessage();
                                    }
                                    if (itemMethod.equals(SearchMethodEnum.NotIncluded.getMessage())) {
                                        itemMethod = SearchMethodEnum.NotLike.getMessage();
                                    }
                                    value = orgId;
                                } else if (AuthorizeConditionEnum.ORGANIZEANDUNDER.getCondition().equals(itemValue)) { //组织及子组织
                                    String orgId = userInfo.getOrganizeId();
                                    if (StringUtil.isNotEmpty(userInfo.getDepartmentId())) {
                                        orgId = userInfo.getDepartmentId();
                                    }
                                    List<String> underOrganizations = organizeService.getUnderOrganizations(orgId, false);
                                    underOrganizations.add(orgId);
                                    value = underOrganizations;
                                } else if (AuthorizeConditionEnum.USERANDUNDER.getCondition().equals(itemValue)) { //用户及用户下属
                                    List<String> idsList = new ArrayList<>();
                                    if (userInfo.getSubordinateIds().size() > 0) {
                                        idsList = userInfo.getSubordinateIds();
                                    }
                                    idsList.add(userInfo.getUserId());
                                    value = idsList;
                                } else if (AuthorizeConditionEnum.BRANCHMANAGEORG.getCondition().equals(itemValue)) { //分管组织
                                    List<OrganizeAdministratorEntity> organizeAdministratorEntity = organizeAdminIsTratorService.getOrganizeAdministratorEntity(userInfo.getUserId(), PermissionConst.ORGANIZE, false);
                                    //子
                                    List<OrganizeAdministratorEntity> organizeAdministratorEntity1 = new ArrayList<>(organizeAdministratorEntity);
                                    //父
                                    List<OrganizeAdministratorEntity> organizeAdministratorEntity2 = new ArrayList<>(organizeAdministratorEntity);
                                    List<String> allIdList = new ArrayList<>();
                                    //子
                                    List<String> childList = organizeAdministratorEntity1.stream().filter(orgAdmin -> orgAdmin.getSubLayerSelect() == 1).map(orgAdmin -> orgAdmin.getOrganizeId()).collect(Collectors.toList());
                                    //父
                                    List<String> fathetList = organizeAdministratorEntity2.stream().filter(orgAdmin -> orgAdmin.getThisLayerSelect() == 1).map(orgAdmin -> orgAdmin.getOrganizeId()).collect(Collectors.toList());
                                    for (String org : childList) {
                                        List<String> underOrganizations = organizeService.getUnderOrganizations(org, false);
                                        if (underOrganizations.size() > 0) {
                                            allIdList.addAll(underOrganizations);
                                        }
                                    }
                                    if (fathetList.size() > 0) {
                                        allIdList.addAll(fathetList);
                                    }
                                    //空集合处理
                                    if (allIdList.size() == 0) {
                                        allIdList.add("futureNullList");
                                    }
                                    value = allIdList;
                                } else if (AuthorizeConditionEnum.BRANCHMANAGEORGANIZEUNDER.getCondition().equals(itemValue)) { //分管组织及子组织
                                    List<OrganizeAdministratorEntity> organizeAdministratorEntity = organizeAdminIsTratorService.getOrganizeAdministratorEntity(userInfo.getUserId(), PermissionConst.ORGANIZE, false);

                                    List<OrganizeAdministratorEntity> organizeAdministratorEntity1 = new ArrayList<>(organizeAdministratorEntity);

                                    List<OrganizeAdministratorEntity> organizeAdministratorEntity2 = new ArrayList<>(organizeAdministratorEntity);

                                    List<String> allIdList = new ArrayList<>();
                                    //需要子集
                                    List<String> childList = new ArrayList<>();

                                    List<String> thisList = organizeAdministratorEntity1.stream().filter(orgAdmin -> orgAdmin.getThisLayerSelect() == 1)
                                            .map(orgAdmin -> orgAdmin.getOrganizeId()).collect(Collectors.toList());

                                    List<String> subList = organizeAdministratorEntity2.stream().filter(orgAdmin -> orgAdmin.getSubLayerSelect() == 1)
                                            .map(orgAdmin -> orgAdmin.getOrganizeId()).collect(Collectors.toList());

                                    if (thisList.size() > 0) {
                                        allIdList.addAll(thisList);
                                        childList.addAll(thisList);
                                    }
                                    if (subList.size() > 0) {
                                        childList.addAll(subList);
                                    }

                                    for (String orgID : childList) {
                                        List<String> underOrganizations = organizeService.getUnderOrganizations(orgID, false);
                                        if (underOrganizations.size() > 0) {
                                            allIdList.addAll(underOrganizations);
                                        }
                                    }
                                    if (allIdList.size() == 0) {
                                        allIdList.add("futureNullList");
                                    }
                                    value = allIdList;
                                } else {//任意文本
                                    value = itemValue;
                                    if (itemMethod.equals(SearchMethodEnum.Included.getMessage())) {
                                        itemMethod = SearchMethodEnum.Like.getMessage();
                                    }
                                    if (itemMethod.equals(SearchMethodEnum.NotIncluded.getMessage())) {
                                        itemMethod = SearchMethodEnum.NotLike.getMessage();
                                    }
                                }
                                if ("and".equalsIgnoreCase(conditionItem.getLogic())) {
                                    if (itemMethod.equals(SearchMethodEnum.Equal.getMessage())) {
                                        tw.eq(itemField, value);
                                    } else if (itemMethod.equals(SearchMethodEnum.NotEqual.getMessage())) {
                                        tw.ne(itemField, value);
                                    } else if (itemMethod.equals(SearchMethodEnum.LessThan.getMessage())) {
                                        tw.lt(itemField, value);
                                    } else if (itemMethod.equals(SearchMethodEnum.LessThanOrEqual.getMessage())) {
                                        tw.le(itemField, value);
                                    } else if (itemMethod.equals(SearchMethodEnum.GreaterThan.getMessage())) {
                                        tw.gt(itemField, value);
                                    } else if (itemMethod.equals(SearchMethodEnum.GreaterThanOrEqual.getMessage())) {
                                        tw.ge(itemField, value);
                                    } else if (itemMethod.equals(SearchMethodEnum.Included.getMessage())) {
                                        tw.in(itemField, value);
                                    } else if (itemMethod.equals(SearchMethodEnum.NotIncluded.getMessage())) {
                                        tw.notIn(itemField, value);
                                    } else if (itemMethod.equals(SearchMethodEnum.Like.getMessage())) {
                                        tw.like(itemField, value);
                                    } else if (itemMethod.equals(SearchMethodEnum.NotLike.getMessage())) {
                                        tw.notLike(itemField, value);
                                    }
                                } else {
                                    if (itemMethod.equals(SearchMethodEnum.Equal.getMessage())) {
                                        tw.or(
                                                qw -> qw.eq(itemField, value)
                                        );
                                    } else if (itemMethod.equals(SearchMethodEnum.NotEqual.getMessage())) {
                                        tw.or(
                                                qw -> qw.ne(itemField, value)
                                        );
                                    } else if (itemMethod.equals(SearchMethodEnum.LessThan.getMessage())) {
                                        tw.or(
                                                qw -> qw.lt(itemField, value)
                                        );
                                    } else if (itemMethod.equals(SearchMethodEnum.LessThanOrEqual.getMessage())) {
                                        tw.or(
                                                qw -> qw.le(itemField, value)
                                        );
                                    } else if (itemMethod.equals(SearchMethodEnum.GreaterThan.getMessage())) {
                                        tw.or(
                                                qw -> qw.gt(itemField, value)
                                        );
                                    } else if (itemMethod.equals(SearchMethodEnum.GreaterThanOrEqual.getMessage())) {
                                        tw.or(
                                                qw -> qw.ge(itemField, value)
                                        );
                                    } else if (itemMethod.equals(SearchMethodEnum.Included.getMessage())) {
                                        tw.or(
                                                qw -> qw.in(itemField, value)
                                        );
                                    } else if (itemMethod.equals(SearchMethodEnum.NotIncluded.getMessage())) {
                                        tw.or(
                                                qw -> qw.notIn(itemField, value)
                                        );
                                    } else if (itemMethod.equals(SearchMethodEnum.Like.getMessage())) {
                                        tw.or(
                                                qw -> qw.like(itemField, value)
                                        );
                                    } else if (itemMethod.equals(SearchMethodEnum.NotLike.getMessage())) {
                                        tw.or(
                                                qw -> qw.notLike(itemField, value)
                                        );
                                    }
                                }
                            }
                        }
                    }
                });
            }
        }
        return ObjectUtil.serialize(queryWhere);
    }

    @Override
    public List<ButtonModel> findButton(String objectId) {
        return this.baseMapper.findButton(objectId);
    }

    @Override
    public List<ColumnModel> findColumn(String objectId) {
        return this.baseMapper.findColumn(objectId);
    }

    @Override
    public List<ResourceModel> findResource(String objectId) {
        return this.baseMapper.findResource(objectId);
    }

    @Override
    public List<ModuleFormModel> findForms(String objectId) {
        return this.baseMapper.findForms(objectId);
    }

    @Override
    public List<ButtonModel> findButtonAdmin(Integer mark) {
        return this.baseMapper.findButtonAdmin(mark);
    }

    @Override
    public List<ColumnModel> findColumnAdmin(Integer mark) {
        return this.baseMapper.findColumnAdmin(mark);
    }

    @Override
    public List<ResourceModel> findResourceAdmin(Integer mark) {
        return this.baseMapper.findResourceAdmin(mark);
    }

    @Override
    public List<ModuleFormModel> findFormsAdmin(Integer mark) {
        return this.baseMapper.findFormsAdmin(mark);
    }

    @Override
    public List<AuthorizeEntity> getAuthorizeByItem(String itemType, String itemId) {
        QueryWrapper<AuthorizeEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(AuthorizeEntity::getItemType, itemType);
        queryWrapper.lambda().eq(AuthorizeEntity::getItemId, itemId);
        return this.list(queryWrapper);
    }

    @Override
    public AuthorizeVO getAuthorizeByUser(boolean singletonOrg) {
        AuthorizeVO authorizeModel = this.getAuthorize(false, singletonOrg);
        AuthorizeVO mainSystemAuthorize = new AuthorizeVO();
        // 分管的话，给菜单及下面的参数赋值进去
        List<String> moduleIds = organizeAdminIsTratorService.getOrganizeAdministratorEntity(UserProvider.getLoginUserId(), PermissionConst.MODULE, false)
                .stream().map(OrganizeAdministratorEntity::getOrganizeId).collect(Collectors.toList());
        List<SystemBaeModel> systemList = authorizeModel.getSystemList();
        List<ModuleModel> modelList = authorizeModel.getModuleList();
        List<ButtonModel> buttonList = authorizeModel.getButtonList();
        List<ColumnModel> columnList = authorizeModel.getColumnList();
        List<ModuleFormModel> formsList = authorizeModel.getFormsList();
        List<ResourceModel> resourceList = authorizeModel.getResourceList();
        List<String> moduleAuthorize = new ArrayList<>();
        List<String> moduleUrlAddressAuthorize = new ArrayList<>();
        if (configValueUtil.isMultiTenancy()) {
            TenantAuthorizeModel tenantAuthorizeModel = TenantDataSourceUtil.getCacheModuleAuthorize(UserProvider.getUser().getTenantId());
            moduleAuthorize = tenantAuthorizeModel.getModuleIdList();
            moduleUrlAddressAuthorize = tenantAuthorizeModel.getUrlAddressList();
        }
        if (UserProvider.getUser().getIsAdministrator()) {
            mainSystemAuthorize = this.getMainSystemAuthorize(new ArrayList<>(), moduleAuthorize, moduleUrlAddressAuthorize, singletonOrg);
        } else if (moduleIds.size() > 0){
            mainSystemAuthorize = this.getMainSystemAuthorize(moduleIds, moduleAuthorize, moduleUrlAddressAuthorize, singletonOrg);
        } else {
            return new AuthorizeVO(modelList, buttonList, columnList, resourceList, formsList, systemList);
        }
        systemList.addAll(mainSystemAuthorize.getSystemList());
        modelList.addAll(mainSystemAuthorize.getModuleList());
        buttonList.addAll(mainSystemAuthorize.getButtonList());
        columnList.addAll(mainSystemAuthorize.getColumnList());
        resourceList.addAll(mainSystemAuthorize.getResourceList());
        formsList.addAll(mainSystemAuthorize.getFormsList());
        return new AuthorizeVO(modelList.stream().distinct().collect(Collectors.toList()),
                buttonList.stream().distinct().collect(Collectors.toList()),
                columnList.stream().distinct().collect(Collectors.toList()),
                resourceList.stream().distinct().collect(Collectors.toList()),
                formsList.stream().distinct().collect(Collectors.toList()),
                systemList.stream().distinct().collect(Collectors.toList()));
    }

}
