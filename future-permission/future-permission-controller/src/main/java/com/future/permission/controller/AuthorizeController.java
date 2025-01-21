package com.future.permission.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.future.base.controller.SuperController;
import com.future.common.annotation.OrganizeAdminIsTrator;
import com.future.common.base.ActionResult;
import com.future.common.constant.MsgCode;
import com.future.common.constant.PlatformConst;
import com.future.common.model.tenant.TenantAuthorizeModel;
import com.future.common.util.JsonUtil;
import com.future.common.util.StringUtil;
import com.future.common.util.UserProvider;
import com.future.common.util.XSSEscape;
import com.future.common.util.treeutil.SumTree;
import com.future.common.util.treeutil.newtreeutil.TreeDotUtils;
import com.future.database.util.TenantDataSourceUtil;
import com.future.module.system.DictionaryDataApi;
import com.future.module.system.ModuleApi;
import com.future.module.system.ModuleButtonApi;
import com.future.module.system.ModuleColumnApi;
import com.future.module.system.ModuleDataAuthorizeSchemeApi;
import com.future.module.system.ModuleFormApi;
import com.future.module.system.SystemApi;
import com.future.module.system.entity.DictionaryDataEntity;
import com.future.module.system.entity.ModuleButtonEntity;
import com.future.module.system.entity.ModuleColumnEntity;
import com.future.module.system.entity.ModuleDataAuthorizeSchemeEntity;
import com.future.module.system.entity.ModuleEntity;
import com.future.module.system.entity.ModuleFormEntity;
import com.future.module.system.entity.SystemEntity;
import com.future.module.system.model.base.SystemApiByIdsModel;
import com.future.module.system.model.base.SystemBaeModel;
import com.future.module.system.model.button.ButtonModel;
import com.future.module.system.model.column.ColumnModel;
import com.future.module.system.model.form.ModuleFormModel;
import com.future.module.system.model.module.ModuleApiByIdsModel;
import com.future.module.system.model.module.ModuleApiModel;
import com.future.module.system.model.module.ModuleModel;
import com.future.module.system.model.resource.ResourceModel;
import com.future.permission.AuthorizeApi;
import com.future.permission.constant.AuthorizeConst;
import com.future.permission.entity.AuthorizeEntity;
import com.future.permission.entity.ColumnsPurviewEntity;
import com.future.permission.entity.OrganizeEntity;
import com.future.permission.entity.PositionEntity;
import com.future.permission.entity.RoleEntity;
import com.future.permission.entity.UserEntity;
import com.future.permission.mapper.AuthorizeMapper;
import com.future.permission.model.authorize.AuthorizeConditionModel;
import com.future.permission.model.authorize.AuthorizeDataModel;
import com.future.permission.model.authorize.AuthorizeDataReturnModel;
import com.future.permission.model.authorize.AuthorizeDataReturnVO;
import com.future.permission.model.authorize.AuthorizeDataUpForm;
import com.future.permission.model.authorize.AuthorizeItemObjIdsVO;
import com.future.permission.model.authorize.AuthorizeVO;
import com.future.permission.model.authorize.DataValuesQuery;
import com.future.permission.model.authorize.SaveAuthForm;
import com.future.permission.model.authorize.SaveBatchForm;
import com.future.permission.model.columnspurview.ColumnsPurviewUpForm;
import com.future.permission.service.AuthorizeService;
import com.future.permission.service.ColumnsPurviewService;
import com.future.reids.config.ConfigValueUtil;
import com.future.visualdev.portal.model.PortalListVO;
import com.future.visualdev.portal.model.PortalModel;
import com.future.visualdev.portal.model.PortalVO;
import com.future.visualdev.portal.model.SavePortalAuthModel;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 操作权限
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2019年9月26日 上午9:18
 */
@Tag(name = "操作权限", description = "Authorize")
@RestController
@RequestMapping("/Authority")
public class AuthorizeController extends SuperController<AuthorizeService, AuthorizeEntity> implements AuthorizeApi {

    @Autowired
    private ModuleApi moduleApi;
    @Autowired
    private ModuleButtonApi buttonApi;
    @Autowired
    private ModuleColumnApi columnApi;
    @Autowired
    private ModuleFormApi formApi;
    @Autowired
    private ModuleDataAuthorizeSchemeApi schemeApi;
    @Autowired
    private AuthorizeService authorizeService;
    @Autowired
    private DictionaryDataApi dictionaryDataApi;
    @Autowired
    private ColumnsPurviewService columnsPurviewService;
    @Autowired
    private AuthorizeMapper authorizeMapper;
    @Autowired
    private SystemApi systemApi;
    @Autowired
    private ConfigValueUtil configValueUtil;


    /**
     * 权限数据
     *
     * @param objectId 对象主键
     * @param dataValuesQuery 权限值
     * @return
     */
    @Operation(summary = "获取岗位/角色/用户权限树形结构")
    @Parameters({
            @Parameter(name = "objectId", description = "对象主键", required = true),
            @Parameter(name = "dataValuesQuery", description = "权限值", required = true)
    })
    @SaCheckPermission(value = {"permission.authorize", "permission.role"}, mode = SaMode.OR)
    @PostMapping("/Data/{objectId}/Values")
    public ActionResult<AuthorizeDataReturnVO> getValuesData(@PathVariable("objectId") String objectId, @RequestBody DataValuesQuery dataValuesQuery) {
        AuthorizeVO authorizeModel = authorizeService.getAuthorize(false, false);
        List<AuthorizeEntity> list = authorizeService.list(new QueryWrapper<AuthorizeEntity>().lambda().eq(AuthorizeEntity::getObjectId, objectId));
        if (!StringUtil.isEmpty(dataValuesQuery.getType())) {
            switch (dataValuesQuery.getType()) {
                case "system":
                    AuthorizeDataReturnVO authorizeDataReturnVO = this.system(list, authorizeModel);
                    return ActionResult.success(authorizeDataReturnVO);
                case "module":
                    List<String> systemId = new ArrayList<>();
                    if (!StringUtil.isEmpty(dataValuesQuery.getModuleIds())) {
                        systemId = Arrays.asList(dataValuesQuery.getModuleIds().split(","));
                    }
                    List<ModuleEntity> moduleList = moduleApi.getList(new ModuleApiModel(false, new ArrayList<>(), new ArrayList<>(), false)).stream().filter(
                            m -> "1".equals(String.valueOf(m.getEnabledMark()))
                    ).collect(Collectors.toList());
                    AuthorizeDataReturnVO dataReturnVO = this.module1(moduleList, list, authorizeModel, systemId);
                    return ActionResult.success(dataReturnVO);
                case "button":
                    List<ModuleEntity> moduleList1 = moduleApi.getList(new ModuleApiModel(false, new ArrayList<>(), new ArrayList<>(), false)).stream().filter(
                            m -> "1".equals(String.valueOf(m.getEnabledMark()))
                    ).collect(Collectors.toList());
                    //挑选出的list
                    List<ModuleEntity> selectorModuleList = new ArrayList<>();
                    List<SystemEntity> selectorSystemList = new ArrayList<>();
                    if (!StringUtil.isEmpty(dataValuesQuery.getModuleIds())) {
                        List<String> moduleId1 = Arrays.asList(dataValuesQuery.getModuleIds().split(","));
                        selectorModuleList = moduleList1.stream().filter(t -> moduleId1.contains(t.getId())).collect(Collectors.toList());
                        selectorSystemList = systemApi.getListByIds(new SystemApiByIdsModel(moduleId1, null));
                    }
                    AuthorizeDataReturnVO dataReturnVo1 = this.moduleButton(selectorModuleList, selectorSystemList, list, authorizeModel);
                    return ActionResult.success(dataReturnVo1);

                case "column":
                    List<ModuleEntity> moduleList2 = moduleApi.getList(new ModuleApiModel(false, new ArrayList<>(), new ArrayList<>(), false)).stream().filter(
                            m -> "1".equals(String.valueOf(m.getEnabledMark()))
                    ).collect(Collectors.toList());
                    //挑选出的list
                    List<ModuleEntity> selectList2 = new ArrayList<>();
                    List<SystemEntity> selectorSystemList1 = new ArrayList<>();
                    if (!StringUtil.isEmpty(dataValuesQuery.getModuleIds())) {
                        List<String> moduleId2 = Arrays.asList(dataValuesQuery.getModuleIds().split(","));
                        selectList2 = moduleList2.stream().filter(t -> moduleId2.contains(t.getId())).collect(Collectors.toList());
                        selectorSystemList1 = systemApi.getListByIds(new SystemApiByIdsModel(moduleId2, null));
                    }
                    AuthorizeDataReturnVO dataReturnVo2 = this.moduleColumn(selectList2, selectorSystemList1, list, authorizeModel);
                    return ActionResult.success(dataReturnVo2);

                case "resource":
                    List<ModuleEntity> moduleList3 = moduleApi.getList(new ModuleApiModel(false, new ArrayList<>(), new ArrayList<>(), false)).stream().filter(
                            m -> "1".equals(String.valueOf(m.getEnabledMark()))
                    ).collect(Collectors.toList());
                    //挑选出的list
                    List<ModuleEntity> selectList3 = new ArrayList<>();
                    List<SystemEntity> selectorSystemList2 = new ArrayList<>();
                    if (!StringUtil.isEmpty(dataValuesQuery.getModuleIds())) {
                        List<String> moduleId3 = Arrays.asList(dataValuesQuery.getModuleIds().split(","));
                        selectList3 = moduleList3.stream().filter(t -> moduleId3.contains(t.getId())).collect(Collectors.toList());
                        selectorSystemList2 = systemApi.getListByIds(new SystemApiByIdsModel(moduleId3, null));
                    }
                    AuthorizeDataReturnVO dataReturnVo3 = this.resourceData(selectList3, selectorSystemList2, list, authorizeModel);
                    return ActionResult.success(dataReturnVo3);

                case "form":
                    List<ModuleEntity> moduleList4 = moduleApi.getList(new ModuleApiModel(false, new ArrayList<>(), new ArrayList<>(), false)).stream().filter(
                            m -> "1".equals(String.valueOf(m.getEnabledMark()))
                    ).collect(Collectors.toList());
                    //挑选出的list
                    List<ModuleEntity> selectList4 = new ArrayList<>();
                    List<SystemEntity> selectorSystemList3 = new ArrayList<>();
                    if (!StringUtil.isEmpty(dataValuesQuery.getModuleIds())) {
                        List<String> moduleId4 = Arrays.asList(dataValuesQuery.getModuleIds().split(","));
                        selectList4 = moduleList4.stream().filter(t -> moduleId4.contains(t.getId())).collect(Collectors.toList());
                        selectorSystemList3 = systemApi.getListByIds(new SystemApiByIdsModel(moduleId4, null));
                    }
                    AuthorizeDataReturnVO dataReturnVo4 = this.moduleForm(selectList4, selectorSystemList3, list, authorizeModel);
                    return ActionResult.success(dataReturnVo4);

                default:
            }
        }
        return ActionResult.fail("类型不能为空");
    }

    /**
     * 获取门户权限
     *
     * @return
     */
    @Operation(summary = "获取门户权限")
    @Parameters({
            @Parameter(name = "id", description = "对象主键", required = true)
    })
    @SaCheckPermission(value = {"permission.authorize", "permission.role", "onlineDev.visualPortal"}, mode = SaMode.OR)
    @GetMapping("/Portal/{id}")
    public ActionResult<PortalVO> getPortalAuth(@PathVariable("id") String id) {
        PortalVO vo = new PortalVO();
        List<PortalModel> myPortalList = new ArrayList<>();
        List<PortalModel> permissionGroupPortalList = new ArrayList<>();
        // 权限组权限
        List<AuthorizeEntity> permissionGroupAuthorize = authorizeService.getListByRoleId(id);
        List<String> moduleAuthorize = new ArrayList<>();
        if (configValueUtil.isMultiTenancy()) {
            TenantAuthorizeModel tenantAuthorizeModel = TenantDataSourceUtil.getCacheModuleAuthorize(UserProvider.getUser().getTenantId());
            moduleAuthorize = tenantAuthorizeModel.getModuleIdList();
        }
        List<SystemEntity> permissionGroupSystemList = systemApi.getListByIds(
                new SystemApiByIdsModel(
                permissionGroupAuthorize.stream()
                        .filter(t -> AuthorizeConst.SYSTEM.equals(t.getItemType()))
                        .map(AuthorizeEntity::getItemId)
                        .collect(Collectors.toList()), moduleAuthorize)
        );
        AuthorizeVO authorize = authorizeService.getAuthorize(false, false);
        List<String> permissionGroupSystemIdList = permissionGroupSystemList.stream().map(SystemEntity::getId).collect(Collectors.toList());
        List<SystemEntity> mySystemList = JsonUtil.getJsonToList(authorize.getSystemList(), SystemEntity.class);
        // 我的数据
        mySystemList = mySystemList.stream().filter(t -> permissionGroupSystemIdList.contains(t.getId())).collect(Collectors.toList());
        long dateTime = System.currentTimeMillis();
        authorizeService.getPortal(mySystemList, myPortalList, dateTime, new ArrayList<>());
        // 权限组数据
        authorizeService.getPortal(permissionGroupSystemList, permissionGroupPortalList, dateTime, new ArrayList<>());

        // 验证他有我没有的
        List<String> noContainsList = permissionGroupPortalList.stream().filter(t -> !myPortalList.contains(t)).map(PortalModel::getId).collect(Collectors.toList());

        myPortalList.addAll(permissionGroupPortalList);
        myPortalList.forEach(t -> {
            if (noContainsList.contains(t.getId())) {
                t.setDisabled(true);
            }
        });
        List<String> allId = myPortalList.stream().distinct().map(PortalModel::getId).collect(Collectors.toList());
        List<SumTree<PortalModel>> trees = TreeDotUtils.convertListToTreeDot(myPortalList.stream().sorted(Comparator.comparing(PortalModel::getSortCode).thenComparing(PortalModel::getCreatorTime, Comparator.reverseOrder())).collect(Collectors.toList()));
        vo.setList(JsonUtil.getJsonToList(trees, PortalListVO.class));
        vo.setAll(allId);
        List<String> collect = permissionGroupAuthorize.stream()
                .filter(t -> AuthorizeConst.AUTHORIZE_PORTAL_MANAGE.equals(t.getItemType()))
                .map(AuthorizeEntity::getItemId)
                .collect(Collectors.toList());
        vo.setIds(permissionGroupPortalList.stream().filter(t -> collect.contains(t.getId())).map(PortalModel::getId).collect(Collectors.toList()));
        return ActionResult.success(vo);
    }

    /**
     * 保存门户权限
     *
     * @return
     */
    @Operation(summary = "保存门户权限")
    @Parameters({
            @Parameter(name = "itemId", description = "对象主键", required = true)
    })
    @SaCheckPermission(value = {"permission.authorize", "permission.role", "onlineDev.visualPortal"}, mode = SaMode.OR)
    @PostMapping("/Portal/{id}")
    public ActionResult<String> savePortalAuth(@PathVariable("id") String id, @RequestBody SavePortalAuthModel model) {
        authorizeService.savePortalAuth(id, model.getIds());
        return ActionResult.success(MsgCode.SU005.get());
    }


    /**
     * 对象数据
     *
     * @param itemId 对象主键
     * @param objectType 对象主键
     * @return
     */
    @Operation(summary = "对象数据")
    @Parameters({
            @Parameter(name = "itemId", description = "对象主键", required = true),
            @Parameter(name = "objectType", description = "对象类型", required = true)
    })
    @SaCheckPermission(value = {"permission.authorize", "permission.role", "onlineDev.visualPortal"}, mode = SaMode.OR)
    @GetMapping("/Model/{itemId}/{objectType}")
    public ActionResult<AuthorizeItemObjIdsVO> getObjectAuth(@PathVariable("itemId") String itemId, @PathVariable("objectType") String objectType) {
        List<AuthorizeEntity> authorizeList = authorizeService.getListByObjectAndItem(itemId, objectType);
        List<String> ids = authorizeList.stream().map(u -> u.getObjectId()).collect(Collectors.toList());
        AuthorizeItemObjIdsVO vo = new AuthorizeItemObjIdsVO();
        vo.setIds(ids);
        return ActionResult.success(vo);
    }

    @Operation(summary = "门户管理授权")
    @Parameters({
            @Parameter(name = "itemId", description = "对象主键", required = true),
            @Parameter(name = "saveAuthForm", description = "保存权限模型", required = true)})
    @PutMapping("/Model/{portalManageId}")
    @SaCheckPermission(value = {"permission.authorize", "permission.role"}, mode = SaMode.OR)
    public ActionResult<String> savePortalManage(@PathVariable("portalManageId") String portalManageId, @RequestBody SaveAuthForm saveAuthForm) {
        authorizeService.savePortalManage(portalManageId, saveAuthForm);
        return ActionResult.success(MsgCode.SU005.get());
    }

    /**
     * 保存
     *
     * @param objectId 对象主键
     * @param authorizeDataUpForm 修改权限模型
     * @return
     */
    @OrganizeAdminIsTrator
    @Operation(summary = "保存权限")
    @Parameters({
            @Parameter(name = "objectId", description = "对象主键", required = true),
            @Parameter(name = "authorizeDataUpForm", description = "修改权限模型", required = true)
    })
    @SaCheckPermission(value = {"permission.authorize", "permission.role"}, mode = SaMode.OR)
    @PutMapping("/Data/{objectId}")
    public ActionResult save(@PathVariable("objectId") String objectId, @RequestBody AuthorizeDataUpForm authorizeDataUpForm) {
        authorizeService.save(objectId, authorizeDataUpForm);
        return ActionResult.success(MsgCode.SU005.get());
    }

    /**
     * 保存批量
     *
     * @param saveBatchForm 批量保存模型
     * @return
     */
    @OrganizeAdminIsTrator
    @Operation(summary = "批量保存权限")
    @Parameters({
            @Parameter(name = "saveBatchForm", description = "批量保存模型", required = true)
    })
    @SaCheckPermission(value = {"permission.authorize"}, mode = SaMode.OR)
    @PostMapping("/Data/Batch")
    public ActionResult saveBatch(@RequestBody SaveBatchForm saveBatchForm) {
        // TODO 全局角色权限
        authorizeService.saveBatch(saveBatchForm, true);
        return ActionResult.success(MsgCode.SU005.get());
    }

    /**
     * 获取模块列表展示字段
     *
     * @param moduleId 菜单Id
     * @return
     */
    @Operation(summary = "获取模块列表展示字段")
    @Parameters({
            @Parameter(name = "moduleId", description = "菜单id", required = true)
    })
    @GetMapping("/GetColumnsByModuleId/{moduleId}")
    public ActionResult getColumnsByModuleId(@PathVariable("moduleId") String moduleId) {
        ColumnsPurviewEntity entity = columnsPurviewService.getInfo(moduleId);
        List<Map<String, Object>> jsonToListMap = null;
        if (entity != null) {
            jsonToListMap = JsonUtil.getJsonToListMap(entity.getFieldList());
        }
        return ActionResult.success(jsonToListMap != null ? jsonToListMap : new ArrayList<>(16));
    }

    /**
     * 配置模块列表展示字段
     *
     * @param columnsPurviewUpForm 修改模型
     * @return
     */
    @Operation(summary = "配置模块列表展示字段")
    @Parameters({
            @Parameter(name = "columnsPurviewUpForm", description = "修改模型", required = true)
    })
    @PutMapping("/SetColumnsByModuleId")
    public ActionResult setColumnsByModuleId(@RequestBody ColumnsPurviewUpForm columnsPurviewUpForm) {
        ColumnsPurviewEntity entity = JsonUtil.getJsonToBean(columnsPurviewUpForm, ColumnsPurviewEntity.class);
        columnsPurviewService.update(columnsPurviewUpForm.getModuleId(), entity);
        return ActionResult.success(MsgCode.SU005.get());
    }

    /**
     * 功能权限
     *
     * @param authorizeList 已有权限
     * @return
     */
    private AuthorizeDataReturnVO system(List<AuthorizeEntity> authorizeList, AuthorizeVO authorizeModel) {
        AuthorizeDataReturnVO vo = new AuthorizeDataReturnVO();
        List<SystemBaeModel> systemList = authorizeModel.getSystemList();
        // 哪些是系统的
        List<AuthorizeEntity> collect = authorizeList.stream().filter(t -> AuthorizeConst.SYSTEM.equals(t.getItemType())).collect(Collectors.toList());
        List<SystemEntity> systemEntityList = systemApi.getListByIds(new SystemApiByIdsModel(collect.stream().map(AuthorizeEntity::getItemId).collect(Collectors.toList()), null));
        List<AuthorizeDataReturnModel> authorizeModelList = JsonUtil.getJsonToList(systemEntityList, AuthorizeDataReturnModel.class);
        List<AuthorizeDataReturnModel> jsonToList = JsonUtil.getJsonToList(systemList, AuthorizeDataReturnModel.class);
        // 取交集并集处理
        List<AuthorizeDataReturnModel> containsList = authorizeModelList.stream().filter(t -> !jsonToList.contains(t)).collect(Collectors.toList());
        List<String> collect1 = containsList.stream().map(AuthorizeDataReturnModel::getId).collect(Collectors.toList());
        collect1.addAll(systemEntityList.stream().map(SystemEntity::getId).collect(Collectors.toList()));
        containsList.forEach(t -> t.setDisabled(true));
        jsonToList.addAll(containsList);
        vo.setList(jsonToList.stream().sorted(Comparator.comparing(AuthorizeDataReturnModel::getSortCode).thenComparing(AuthorizeDataReturnModel::getCreatorTime, Comparator.reverseOrder())).collect(Collectors.toList()));
        vo.setAll(jsonToList.stream().map(AuthorizeDataReturnModel::getId).collect(Collectors.toList()));
        vo.setIds(collect1.stream().distinct().collect(Collectors.toList()));
        return vo;
    }

    /**
     * 功能权限
     *
     * @param moduleListAll  所有功能
     * @param authorizeList  已有权限
     * @param authorizeModel 权限集合
     * @param systemId       系统id
     * @return
     */
    private AuthorizeDataReturnVO module1(List<ModuleEntity> moduleListAll, List<AuthorizeEntity> authorizeList, AuthorizeVO authorizeModel, List<String> systemId) {
        AuthorizeDataReturnVO vo = new AuthorizeDataReturnVO();
        List<ModuleModel> moduleList = new ArrayList<>();
        // 权限组本身拥有的菜单
        List<AuthorizeEntity> authorizeLists = authorizeList.stream().filter(t -> AuthorizeConst.MODULE.equals(t.getItemType())).collect(Collectors.toList());
        List<ModuleEntity> moduleByIds = moduleApi.getModuleByIds(new ModuleApiByIdsModel(authorizeLists.stream().map(AuthorizeEntity::getItemId).collect(Collectors.toList()), null, null, false))
                .stream().filter(t -> systemId.contains(t.getSystemId())).collect(Collectors.toList());
        List<ModuleModel> jsonToList = JsonUtil.getJsonToList(moduleByIds, ModuleModel.class);
        // 我的菜单
        List<ModuleModel> moduleList1 = authorizeModel.getModuleList();
        moduleList1 = moduleList1.stream().filter(t -> systemId.contains(t.getSystemId())).collect(Collectors.toList());
        List<String> collect = moduleList1.stream().map(ModuleModel::getId).collect(Collectors.toList());
        List<ModuleModel> collect1 = jsonToList.stream().filter(t -> collect.contains(t.getId())).collect(Collectors.toList());

        List<String> containsList = collect1.stream().map(ModuleModel::getId).collect(Collectors.toList());
        List<String> collect3 = authorizeList.stream().map(AuthorizeEntity::getItemId).collect(Collectors.toList());
        containsList.addAll(collect3);
        containsList.addAll(systemId);
        moduleList.addAll(jsonToList);
        moduleList.addAll(moduleList1);
        moduleList = moduleList.stream().filter(t -> systemId.contains(t.getSystemId())).distinct().collect(Collectors.toList());

        List<ModuleModel> list = new ArrayList<>(moduleList);
        // 存放上级菜单id及上级 systemId,id
        Map<String, String> appIds = new HashMap<>(16);
        Map<String, String> webIds = new HashMap<>(16);
        long datetime = System.currentTimeMillis();
        moduleList.stream().sorted(Comparator.comparing(ModuleModel::getCategory).reversed()).forEach(t -> {
            if ("App".equals(t.getCategory()) && "-1".equals(t.getParentId())) {
                if (!appIds.containsKey(t.getSystemId())) {
                    t.setParentId(t.getSystemId() + "1");
                    ModuleModel appData = new ModuleModel();
                    appData.setId(t.getSystemId() + "1");
                    appData.setSortCode(0L);
                    appData.setCreatorTime(datetime);
                    appData.setFullName("APP菜单");
                    appData.setIcon("icon-ym icon-ym-mobile");
                    appData.setParentId(t.getSystemId());
                    appData.setSystemId(t.getSystemId());
                    list.add(appData);
                    appIds.put(t.getSystemId(), appData.getId());
                } else {
                    t.setParentId(appIds.get(t.getSystemId()) + "");
                }
            }
            else if ("Web".equals(t.getCategory()) && "-1".equals(t.getParentId())) {
                if (!webIds.containsKey(t.getSystemId())) {
                    t.setParentId(t.getSystemId() + "2");
                    ModuleModel webData = new ModuleModel();
                    webData.setId(t.getSystemId() + "2");
                    webData.setSortCode(-1L);
                    webData.setCreatorTime(datetime);
                    webData.setFullName("WEB菜单");
                    webData.setIcon("icon-ym icon-ym-pc");
                    webData.setParentId(t.getSystemId());
                    webData.setSystemId(t.getSystemId());
                    list.add(webData);
                    webIds.put(t.getSystemId(), webData.getId());
                } else {
                    t.setParentId(webIds.get(t.getSystemId()) + "");
                }
            }
            ModuleModel model = JsonUtil.getJsonToBean(t, ModuleModel.class);
            list.add(model);
        });
        list.stream().filter(t -> "-1".equals(t.getParentId())).forEach(t -> t.setParentId(t.getSystemId()));
        List<SystemEntity> systemList = systemApi.getListByIds(new SystemApiByIdsModel(systemId, null));
        List<ModuleModel> jsonToList1 = JsonUtil.getJsonToList(systemList, ModuleModel.class);
        jsonToList1.forEach(t -> {
            t.setParentId("-1");
            t.setSystemId(t.getId());
        });
        list.addAll(jsonToList1);

        List<String> mySystemIdList = authorizeModel.getSystemList().stream().map(SystemBaeModel::getId).collect(Collectors.toList());
        List<String> collect2 = list.stream().filter(t -> !mySystemIdList.contains(t.getSystemId())).map(ModuleModel::getId).collect(Collectors.toList());
        List<AuthorizeDataModel> treeList = JsonUtil.getJsonToList(list, AuthorizeDataModel.class);
        treeList.forEach(t -> {
            if (collect2.contains(t.getId())) {
                t.setDisabled(true);
            }
        });
        treeList = treeList.stream().sorted(Comparator.comparing(AuthorizeDataModel::getSortCode).thenComparing(AuthorizeDataModel::getCreatorTime, Comparator.reverseOrder())).collect(Collectors.toList());
        List<SumTree<AuthorizeDataModel>> trees = TreeDotUtils.convertListToTreeDot(treeList);
        List<AuthorizeDataReturnModel> data = JsonUtil.getJsonToList(trees, AuthorizeDataReturnModel.class);
        vo.setAll(list.stream().map(ModuleModel::getId).distinct().collect(Collectors.toList()));
        vo.setList(data);
        containsList.addAll(collect2);
        vo.setIds(containsList.stream().distinct().collect(Collectors.toList()));
        return vo;
    }

    /**
     * 按钮权限
     *
     * @param moduleList     功能
     * @param selectorSystemList   应用
     * @param authorizeList  已有权限
     * @param authorizeModel 权限集合
     * @return
     */
    AuthorizeDataReturnVO moduleButton(List<ModuleEntity> moduleList, List<SystemEntity> selectorSystemList, List<AuthorizeEntity> authorizeList, AuthorizeVO authorizeModel) {
        AuthorizeDataReturnVO vo = new AuthorizeDataReturnVO();
        // 树
        List<ButtonModel> allButtonList = new ArrayList<>();
        // id
        List<String> ids = new ArrayList<>();
        List<String> noContainsIds = new ArrayList<>();
        // 转map
        Map<String, ModuleEntity> moduleMap = moduleList.stream().collect(Collectors.toMap(ModuleEntity::getId, Function.identity()));
        Set<String> moduleIdIds = moduleMap.keySet();
        Map<String, SystemEntity> systemEntityMap = selectorSystemList.stream().collect(Collectors.toMap(SystemEntity::getId, Function.identity()));
        Set<String> systemIdIds = systemEntityMap.keySet();
        // 我的菜单权限
        List<ButtonModel> myButtonList = authorizeModel.getButtonList();
        myButtonList = myButtonList.stream().filter(t -> moduleIdIds.contains(t.getModuleId())).collect(Collectors.toList());
        // 权限组的权限
        List<AuthorizeEntity> authorizeLists = authorizeList.stream().filter(t -> AuthorizeConst.BUTTON.equals(t.getItemType())).collect(Collectors.toList());
        List<ModuleButtonEntity> buttonByIds = buttonApi.getListByIds(authorizeLists.stream().map(AuthorizeEntity::getItemId).collect(Collectors.toList()))
                .stream().filter(t -> moduleIdIds.contains(t.getModuleId())).collect(Collectors.toList());
        List<ButtonModel> permissionGroupButtonList = JsonUtil.getJsonToList(buttonByIds, ButtonModel.class);
        // 将菜单id设置给按钮的上级
        myButtonList.forEach(t -> t.setParentId(t.getModuleId()));
        permissionGroupButtonList.forEach(t -> t.setParentId(t.getModuleId()));
        // 所有的按钮权限
        allButtonList.addAll(myButtonList);
        allButtonList.addAll(permissionGroupButtonList);
        // 我的按钮id
        List<String> myButtonId = myButtonList.stream().map(ButtonModel::getId).collect(Collectors.toList());
        // 交集 1:1
        List<String> containsButtonList = permissionGroupButtonList.stream().filter(t -> myButtonId.contains(t.getId())).map(ButtonModel::getId).distinct().collect(Collectors.toList());
        // 我没有的
        List<String> noContainsButtonList = permissionGroupButtonList.stream().filter(t -> !containsButtonList.contains(t.getId())).map(ButtonModel::getId).collect(Collectors.toList());
        List<String> collect3 = authorizeList.stream().map(AuthorizeEntity::getItemId).collect(Collectors.toList());
        ids.addAll(collect3);
        ids.addAll(containsButtonList);
        noContainsIds.addAll(noContainsButtonList);
        // 我的菜单
        List<ModuleModel> myModuleList = authorizeModel.getModuleList();
        // 共有菜单
        List<String> containsModuleList = myModuleList.stream().filter(t -> moduleIdIds.contains(t.getId())).map(ModuleModel::getId).collect(Collectors.toList());
        List<String> myModuleIds = myModuleList.stream().map(ModuleModel::getId).collect(Collectors.toList());
        List<String> noContainsModuleList = moduleIdIds.stream().filter(t -> !myModuleIds.contains(t)).collect(Collectors.toList());
        ids.addAll(containsModuleList);
        noContainsIds.addAll(noContainsModuleList);
        // 我的应用
        List<SystemBaeModel> mySystemList = authorizeModel.getSystemList();
        // 共有应用
        List<String> containsSystemList = mySystemList.stream().filter(t -> systemIdIds.contains(t.getId())).map(SystemBaeModel::getId).collect(Collectors.toList());
        List<String> mySystemIds = mySystemList.stream().map(SystemBaeModel::getId).collect(Collectors.toList());
        List<String> noContainsSystemList = systemIdIds.stream().filter(t -> !mySystemIds.contains(t)).collect(Collectors.toList());
        ids.addAll(containsSystemList);
        noContainsIds.addAll(noContainsSystemList);
        // 所有按钮权限的上级
        List<String> allModuleIds = allButtonList.stream().map(ButtonModel::getParentId).distinct().collect(Collectors.toList());
        Map<String, ModuleEntity> allModuleListMap = new HashMap<>();
        // 新建APP菜单上级
        allModuleIds.forEach(t -> {
            ModuleEntity entity = moduleMap.get(t);
            while (entity != null) {
                allModuleListMap.put(entity.getId(), entity);
                entity = moduleMap.get(entity.getParentId());
            }
        });
        // 存放上级菜单id及上级 systemId,id
        Map<String, String> appIds = new HashMap<>(16);
        Map<String, String> webIds = new HashMap<>(16);
        long datetime = System.currentTimeMillis();
        allModuleListMap.values().stream().sorted(Comparator.comparing(ModuleEntity::getCategory).reversed()).forEach(t -> {
            if ("App".equals(t.getCategory()) && "-1".equals(t.getParentId())) {
                if (!appIds.containsKey(t.getSystemId())) {
                    t.setParentId(t.getSystemId() + "1");
                    ButtonModel appData = new ButtonModel();
                    appData.setId(t.getSystemId() + "1");
                    appData.setSortCode(0L);
                    appData.setCreatorTime(datetime);
                    appData.setFullName("APP菜单");
                    appData.setIcon("icon-ym icon-ym-mobile");
                    appData.setParentId(t.getSystemId());
                    allButtonList.add(appData);
                    appIds.put(t.getSystemId(), appData.getId());
                } else {
                    t.setParentId(appIds.get(t.getSystemId()) + "");
                }
            }
            else if ("Web".equals(t.getCategory()) && "-1".equals(t.getParentId())) {
                if (!webIds.containsKey(t.getSystemId())) {
                    t.setParentId(t.getSystemId() + "2");
                    ButtonModel webData = new ButtonModel();
                    webData.setId(t.getSystemId() + "2");
                    webData.setSortCode(-1L);
                    webData.setCreatorTime(datetime);
                    webData.setFullName("WEB菜单");
                    webData.setIcon("icon-ym icon-ym-pc");
                    webData.setParentId(t.getSystemId());
                    allButtonList.add(webData);
                    webIds.put(t.getSystemId(), webData.getId());
                } else {
                    t.setParentId(webIds.get(t.getSystemId()) + "");
                }
            }
            ButtonModel model = JsonUtil.getJsonToBean(t, ButtonModel.class);
            allButtonList.add(model);
        });
        allButtonList.stream().filter(t -> "-1".equals(t.getParentId())).forEach(t -> t.setParentId(t.getSystemId()));
        List<String> systemId = allButtonList.stream().filter(t -> StringUtil.isNotEmpty(t.getParentId())).map(ButtonModel::getParentId).collect(Collectors.toList());
        systemId.forEach(t -> {
            if (Optional.ofNullable(systemEntityMap.get(t)).isPresent()) {
                ButtonModel jsonToBean = JsonUtil.getJsonToBean(systemEntityMap.get(t), ButtonModel.class);
                jsonToBean.setParentId("-1");
                allButtonList.add(jsonToBean);
            }
        });

        List<AuthorizeDataModel> treeList = JsonUtil.getJsonToList(allButtonList, AuthorizeDataModel.class);
        // 处理不可选的
        treeList.forEach(t -> {
            if (noContainsIds.contains(t.getId())) {
                t.setDisabled(true);
            }
        });
        treeList = treeList.stream().sorted(Comparator.comparing(AuthorizeDataModel::getSortCode).thenComparing(AuthorizeDataModel::getCreatorTime, Comparator.reverseOrder())).collect(Collectors.toList());
        List<SumTree<AuthorizeDataModel>> trees = TreeDotUtils.convertListToTreeDot(treeList);
        List<AuthorizeDataReturnModel> data = JsonUtil.getJsonToList(trees, AuthorizeDataReturnModel.class);
        vo.setAll(allButtonList.stream().map(ButtonModel::getId).distinct().collect(Collectors.toList()));
        vo.setList(data);
        ids.addAll(noContainsIds);
        vo.setIds(ids.stream().distinct().collect(Collectors.toList()));
        return vo;
    }

    /**
     * 列表权限
     *
     * @param moduleList       功能
     * @param selectorSystemList 列表
     * @param authorizeList    已有权限
     * @param authorizeModel   权限集合
     * @return
     */
    AuthorizeDataReturnVO moduleColumn(List<ModuleEntity> moduleList, List<SystemEntity> selectorSystemList, List<AuthorizeEntity> authorizeList, AuthorizeVO authorizeModel) {
        AuthorizeDataReturnVO vo = new AuthorizeDataReturnVO();
        // 树
        List<ColumnModel> allButtonList = new ArrayList<>();
        // id
        List<String> ids = new ArrayList<>();
        List<String> noContainsIds = new ArrayList<>();
        // 转map
        Map<String, ModuleEntity> moduleMap = moduleList.stream().collect(Collectors.toMap(ModuleEntity::getId, Function.identity()));
        Set<String> moduleIdIds = moduleMap.keySet();
        Map<String, SystemEntity> systemEntityMap = selectorSystemList.stream().collect(Collectors.toMap(SystemEntity::getId, Function.identity()));
        Set<String> systemIdIds = systemEntityMap.keySet();
        // 我的菜单权限
        List<ColumnModel> myButtonList = authorizeModel.getColumnList();
        myButtonList = myButtonList.stream().filter(t -> moduleIdIds.contains(t.getModuleId())).collect(Collectors.toList());
        // 权限组的权限
        List<AuthorizeEntity> authorizeLists = authorizeList.stream().filter(t -> AuthorizeConst.COLUMN.equals(t.getItemType())).collect(Collectors.toList());
        List<ModuleColumnEntity> buttonByIds = columnApi.getListByIds(authorizeLists.stream().map(AuthorizeEntity::getItemId).collect(Collectors.toList()))
                .stream().filter(t -> moduleIdIds.contains(t.getModuleId())).collect(Collectors.toList());
        List<ColumnModel> permissionGroupButtonList = JsonUtil.getJsonToList(buttonByIds, ColumnModel.class);
        // 将菜单id设置给按钮的上级
        myButtonList.forEach(t -> t.setParentId(t.getModuleId()));
        permissionGroupButtonList.forEach(t -> t.setParentId(t.getModuleId()));
        // 所有的按钮权限
        allButtonList.addAll(myButtonList);
        allButtonList.addAll(permissionGroupButtonList);
        // 我的按钮id
        List<String> myButtonId = myButtonList.stream().map(ColumnModel::getId).collect(Collectors.toList());
        // 交集 1:1
        List<String> containsButtonList = permissionGroupButtonList.stream().filter(t -> myButtonId.contains(t.getId())).map(ColumnModel::getId).distinct().collect(Collectors.toList());
        // 我没有的
        List<String> noContainsButtonList = permissionGroupButtonList.stream().filter(t -> !containsButtonList.contains(t.getId())).map(ColumnModel::getId).collect(Collectors.toList());
        List<String> collect3 = authorizeList.stream().map(AuthorizeEntity::getItemId).collect(Collectors.toList());
        ids.addAll(collect3);
        ids.addAll(containsButtonList);
        noContainsIds.addAll(noContainsButtonList);
        // 我的菜单
        List<ModuleModel> myModuleList = authorizeModel.getModuleList();
        // 共有菜单
        List<String> containsModuleList = myModuleList.stream().filter(t -> moduleIdIds.contains(t.getId())).map(ModuleModel::getId).collect(Collectors.toList());
        List<String> myModuleIds = myModuleList.stream().map(ModuleModel::getId).collect(Collectors.toList());
        List<String> noContainsModuleList = moduleIdIds.stream().filter(t -> !myModuleIds.contains(t)).collect(Collectors.toList());
        ids.addAll(containsModuleList);
        noContainsIds.addAll(noContainsModuleList);
        // 我的应用
        List<SystemBaeModel> mySystemList = authorizeModel.getSystemList();
        // 共有应用
        List<String> containsSystemList = mySystemList.stream().filter(t -> systemIdIds.contains(t.getId())).map(SystemBaeModel::getId).collect(Collectors.toList());
        List<String> mySystemIds = mySystemList.stream().map(SystemBaeModel::getId).collect(Collectors.toList());
        List<String> noContainsSystemList = systemIdIds.stream().filter(t -> !mySystemIds.contains(t)).collect(Collectors.toList());
        ids.addAll(containsSystemList);
        noContainsIds.addAll(noContainsSystemList);
        // 所有按钮权限的上级
        List<String> allModuleIds = allButtonList.stream().map(ColumnModel::getParentId).distinct().collect(Collectors.toList());
        Map<String, ModuleEntity> allModuleListMap = new HashMap<>();
        // 新建APP菜单上级
        allModuleIds.forEach(t -> {
            ModuleEntity entity = moduleMap.get(t);
            while (entity != null) {
                allModuleListMap.put(entity.getId(), entity);
                entity = moduleMap.get(entity.getParentId());
            }
        });
        // 存放上级菜单id及上级 systemId,id
        Map<String, String> appIds = new HashMap<>(16);
        Map<String, String> webIds = new HashMap<>(16);
        long datetime = System.currentTimeMillis();
        allModuleListMap.values().stream().sorted(Comparator.comparing(ModuleEntity::getCategory).reversed()).forEach(t -> {
            if ("App".equals(t.getCategory()) && "-1".equals(t.getParentId())) {
                if (!appIds.containsKey(t.getSystemId())) {
                    t.setParentId(t.getSystemId() + "1");
                    ColumnModel appData = new ColumnModel();
                    appData.setId(t.getSystemId() + "1");
                    appData.setSortCode(0L);
                    appData.setCreatorTime(datetime);
                    appData.setFullName("APP菜单");
                    appData.setIcon("icon-ym icon-ym-mobile");
                    appData.setParentId(t.getSystemId());
                    allButtonList.add(appData);
                    appIds.put(t.getSystemId(), appData.getId());
                } else {
                    t.setParentId(appIds.get(t.getSystemId()) + "");
                }
            }
            else if ("Web".equals(t.getCategory()) && "-1".equals(t.getParentId())) {
                if (!webIds.containsKey(t.getSystemId())) {
                    t.setParentId(t.getSystemId() + "2");
                    ColumnModel webData = new ColumnModel();
                    webData.setId(t.getSystemId() + "2");
                    webData.setSortCode(-1L);
                    webData.setCreatorTime(datetime);
                    webData.setFullName("WEB菜单");
                    webData.setIcon("icon-ym icon-ym-pc");
                    webData.setParentId(t.getSystemId());
                    allButtonList.add(webData);
                    webIds.put(t.getSystemId(), webData.getId());
                } else {
                    t.setParentId(webIds.get(t.getSystemId()) + "");
                }
            }
            ColumnModel model = JsonUtil.getJsonToBean(t, ColumnModel.class);
            allButtonList.add(model);
        });
        allButtonList.stream().filter(t -> "-1".equals(t.getParentId())).forEach(t -> t.setParentId(t.getSystemId()));
        List<String> systemId = allButtonList.stream().filter(t -> StringUtil.isNotEmpty(t.getParentId())).map(ColumnModel::getParentId).collect(Collectors.toList());
        systemId.forEach(t -> {
            if (Optional.ofNullable(systemEntityMap.get(t)).isPresent()) {
                ColumnModel jsonToBean = JsonUtil.getJsonToBean(systemEntityMap.get(t), ColumnModel.class);
                jsonToBean.setParentId("-1");
                allButtonList.add(jsonToBean);
            }
        });
        List<AuthorizeDataModel> treeList = JsonUtil.getJsonToList(allButtonList, AuthorizeDataModel.class);
        // 处理不可选的
        treeList.forEach(t -> {
            if (noContainsIds.contains(t.getId())) {
                t.setDisabled(true);
            }
        });
        treeList = treeList.stream().sorted(Comparator.comparing(AuthorizeDataModel::getSortCode).thenComparing(AuthorizeDataModel::getCreatorTime, Comparator.reverseOrder())).collect(Collectors.toList());
        List<SumTree<AuthorizeDataModel>> trees = TreeDotUtils.convertListToTreeDot(treeList);
        List<AuthorizeDataReturnModel> data = JsonUtil.getJsonToList(trees, AuthorizeDataReturnModel.class);
        vo.setAll(allButtonList.stream().map(ColumnModel::getId).distinct().collect(Collectors.toList()));
        vo.setList(data);
        ids.addAll(noContainsIds);
        vo.setIds(ids.stream().distinct().collect(Collectors.toList()));
        return vo;
    }

    /**
     * 表单权限
     *
     * @param moduleList     功能
     * @param selectorSystemList 应用列表
     * @param authorizeList  已有权限
     * @param authorizeModel 权限集合
     * @return
     */
    AuthorizeDataReturnVO moduleForm(List<ModuleEntity> moduleList, List<SystemEntity> selectorSystemList, List<AuthorizeEntity> authorizeList, AuthorizeVO authorizeModel) {
        AuthorizeDataReturnVO vo = new AuthorizeDataReturnVO();
        // 树
        List<ModuleFormModel> allButtonList = new ArrayList<>();
        // id
        List<String> ids = new ArrayList<>();
        List<String> noContainsIds = new ArrayList<>();
        // 转map
        Map<String, ModuleEntity> moduleMap = moduleList.stream().collect(Collectors.toMap(ModuleEntity::getId, Function.identity()));
        Set<String> moduleIdIds = moduleMap.keySet();
        Map<String, SystemEntity> systemEntityMap = selectorSystemList.stream().collect(Collectors.toMap(SystemEntity::getId, Function.identity()));
        Set<String> systemIdIds = systemEntityMap.keySet();
        // 我的菜单权限
        List<ModuleFormModel> myButtonList = authorizeModel.getFormsList();
        myButtonList = myButtonList.stream().filter(t -> moduleIdIds.contains(t.getModuleId())).collect(Collectors.toList());
        // 权限组的权限
        List<AuthorizeEntity> authorizeLists = authorizeList.stream().filter(t -> AuthorizeConst.FROM.equals(t.getItemType())).collect(Collectors.toList());
        List<ModuleFormEntity> buttonByIds = formApi.getListByIds(authorizeLists.stream().map(AuthorizeEntity::getItemId).collect(Collectors.toList()))
                .stream().filter(t -> moduleIdIds.contains(t.getModuleId())).collect(Collectors.toList());
        List<ModuleFormModel> permissionGroupButtonList = JsonUtil.getJsonToList(buttonByIds, ModuleFormModel.class);
        // 将菜单id设置给按钮的上级
        myButtonList.forEach(t -> t.setParentId(t.getModuleId()));
        permissionGroupButtonList.forEach(t -> t.setParentId(t.getModuleId()));
        // 所有的按钮权限
        allButtonList.addAll(myButtonList);
        allButtonList.addAll(permissionGroupButtonList);
        // 我的按钮id
        List<String> myButtonId = myButtonList.stream().map(ModuleFormModel::getId).collect(Collectors.toList());
        // 交集 1:1
        List<String> containsButtonList = permissionGroupButtonList.stream().filter(t -> myButtonId.contains(t.getId())).map(ModuleFormModel::getId).distinct().collect(Collectors.toList());
        // 我没有的
        List<String> noContainsButtonList = permissionGroupButtonList.stream().filter(t -> !containsButtonList.contains(t.getId())).map(ModuleFormModel::getId).collect(Collectors.toList());
        List<String> collect3 = authorizeList.stream().map(AuthorizeEntity::getItemId).collect(Collectors.toList());
        ids.addAll(collect3);
        ids.addAll(containsButtonList);
        noContainsIds.addAll(noContainsButtonList);
        // 我的菜单
        List<ModuleModel> myModuleList = authorizeModel.getModuleList();
        // 共有菜单
        List<String> containsModuleList = myModuleList.stream().filter(t -> moduleIdIds.contains(t.getId())).map(ModuleModel::getId).collect(Collectors.toList());
        List<String> myModuleIds = myModuleList.stream().map(ModuleModel::getId).collect(Collectors.toList());
        List<String> noContainsModuleList = moduleIdIds.stream().filter(t -> !myModuleIds.contains(t)).collect(Collectors.toList());
        ids.addAll(containsModuleList);
        noContainsIds.addAll(noContainsModuleList);
        // 我的应用
        List<SystemBaeModel> mySystemList = authorizeModel.getSystemList();
        // 共有应用
        List<String> containsSystemList = mySystemList.stream().filter(t -> systemIdIds.contains(t.getId())).map(SystemBaeModel::getId).collect(Collectors.toList());
        List<String> mySystemIds = mySystemList.stream().map(SystemBaeModel::getId).collect(Collectors.toList());
        List<String> noContainsSystemList = systemIdIds.stream().filter(t -> !mySystemIds.contains(t)).collect(Collectors.toList());
        ids.addAll(containsSystemList);
        noContainsIds.addAll(noContainsSystemList);
        // 所有按钮权限的上级
        List<String> allModuleIds = allButtonList.stream().map(ModuleFormModel::getParentId).distinct().collect(Collectors.toList());
        Map<String, ModuleEntity> allModuleListMap = new HashMap<>();
        // 新建APP菜单上级
        allModuleIds.forEach(t -> {
            ModuleEntity entity = moduleMap.get(t);
            while (entity != null) {
                allModuleListMap.put(entity.getId(), entity);
                entity = moduleMap.get(entity.getParentId());
            }
        });
        // 存放上级菜单id及上级 systemId,id
        Map<String, String> appIds = new HashMap<>(16);
        Map<String, String> webIds = new HashMap<>(16);
        long datetime = System.currentTimeMillis();
        allModuleListMap.values().stream().sorted(Comparator.comparing(ModuleEntity::getCategory).reversed()).forEach(t -> {
            if ("App".equals(t.getCategory()) && "-1".equals(t.getParentId())) {
                if (!appIds.containsKey(t.getSystemId())) {
                    t.setParentId(t.getSystemId() + "1");
                    ModuleFormModel appData = new ModuleFormModel();
                    appData.setId(t.getSystemId() + "1");
                    appData.setSortCode(0L);
                    appData.setCreatorTime(datetime);
                    appData.setFullName("APP菜单");
                    appData.setIcon("icon-ym icon-ym-mobile");
                    appData.setParentId(t.getSystemId());
                    allButtonList.add(appData);
                    appIds.put(t.getSystemId(), appData.getId());
                } else {
                    t.setParentId(appIds.get(t.getSystemId()) + "");
                }
            }
            else if ("Web".equals(t.getCategory()) && "-1".equals(t.getParentId())) {
                if (!webIds.containsKey(t.getSystemId())) {
                    t.setParentId(t.getSystemId() + "2");
                    ModuleFormModel webData = new ModuleFormModel();
                    webData.setId(t.getSystemId() + "2");
                    webData.setSortCode(-1L);
                    webData.setCreatorTime(datetime);
                    webData.setFullName("WEB菜单");
                    webData.setIcon("icon-ym icon-ym-pc");
                    webData.setParentId(t.getSystemId());
                    allButtonList.add(webData);
                    webIds.put(t.getSystemId(), webData.getId());
                } else {
                    t.setParentId(webIds.get(t.getSystemId()) + "");
                }
            }
            ModuleFormModel model = JsonUtil.getJsonToBean(t, ModuleFormModel.class);
            allButtonList.add(model);
        });
        allButtonList.stream().filter(t -> "-1".equals(t.getParentId())).forEach(t -> t.setParentId(t.getSystemId()));
        List<String> systemId = allButtonList.stream().filter(t -> StringUtil.isNotEmpty(t.getParentId())).map(ModuleFormModel::getParentId).collect(Collectors.toList());
        systemId.forEach(t -> {
            if (Optional.ofNullable(systemEntityMap.get(t)).isPresent()) {
                ModuleFormModel jsonToBean = JsonUtil.getJsonToBean(systemEntityMap.get(t), ModuleFormModel.class);
                jsonToBean.setParentId("-1");
                allButtonList.add(jsonToBean);
            }
        });
        List<AuthorizeDataModel> treeList = JsonUtil.getJsonToList(allButtonList, AuthorizeDataModel.class);
        // 处理不可选的
        treeList.forEach(t -> {
            if (noContainsIds.contains(t.getId())) {
                t.setDisabled(true);
            }
        });
        treeList = treeList.stream().sorted(Comparator.comparing(AuthorizeDataModel::getSortCode).thenComparing(AuthorizeDataModel::getCreatorTime, Comparator.reverseOrder())).collect(Collectors.toList());
        List<SumTree<AuthorizeDataModel>> trees = TreeDotUtils.convertListToTreeDot(treeList);
        List<AuthorizeDataReturnModel> data = JsonUtil.getJsonToList(trees, AuthorizeDataReturnModel.class);
        vo.setAll(allButtonList.stream().map(ModuleFormModel::getId).distinct().collect(Collectors.toList()));
        vo.setList(data);
        ids.addAll(noContainsIds);
        vo.setIds(ids.stream().distinct().collect(Collectors.toList()));
        return vo;
    }

    /**
     * 数据权限
     *
     * @param moduleList           功能
     * @param selectorSystemList 应用方案
     * @param authorizeList        已有权限
     * @param authorizeModel       权限集合
     * @return
     */
    AuthorizeDataReturnVO resourceData(List<ModuleEntity> moduleList, List<SystemEntity> selectorSystemList, List<AuthorizeEntity> authorizeList, AuthorizeVO authorizeModel) {
        AuthorizeDataReturnVO vo = new AuthorizeDataReturnVO();
        // 树
        List<ResourceModel> allButtonList = new ArrayList<>();
        // id
        List<String> ids = new ArrayList<>();
        List<String> noContainsIds = new ArrayList<>();
        // 转map
        Map<String, ModuleEntity> moduleMap = moduleList.stream().collect(Collectors.toMap(ModuleEntity::getId, Function.identity()));
        Set<String> moduleIdIds = moduleMap.keySet();
        Map<String, SystemEntity> systemEntityMap = selectorSystemList.stream().collect(Collectors.toMap(SystemEntity::getId, Function.identity()));
        Set<String> systemIdIds = systemEntityMap.keySet();
        // 我的菜单权限
        List<ResourceModel> myButtonList = authorizeModel.getResourceList();
        myButtonList = myButtonList.stream().filter(t -> moduleIdIds.contains(t.getModuleId())).collect(Collectors.toList());
        // 权限组的权限
        List<AuthorizeEntity> authorizeLists = authorizeList.stream().filter(t -> AuthorizeConst.RESOURCE.equals(t.getItemType())).collect(Collectors.toList());
        List<ModuleDataAuthorizeSchemeEntity> buttonByIds = schemeApi.getListByIds(authorizeLists.stream().map(AuthorizeEntity::getItemId).collect(Collectors.toList()))
                .stream().filter(t -> moduleIdIds.contains(t.getModuleId())).collect(Collectors.toList());
        List<ResourceModel> permissionGroupButtonList = JsonUtil.getJsonToList(buttonByIds, ResourceModel.class);
        // 将菜单id设置给按钮的上级
        myButtonList.forEach(t -> t.setParentId(t.getModuleId()));
        permissionGroupButtonList.forEach(t -> t.setParentId(t.getModuleId()));
        // 所有的按钮权限
        allButtonList.addAll(myButtonList);
        allButtonList.addAll(permissionGroupButtonList);
        // 我的按钮id
        List<String> myButtonId = myButtonList.stream().map(ResourceModel::getId).collect(Collectors.toList());
        // 交集 1:1
        List<String> containsButtonList = permissionGroupButtonList.stream().filter(t -> myButtonId.contains(t.getId())).map(ResourceModel::getId).distinct().collect(Collectors.toList());
        // 我没有的
        List<String> noContainsButtonList = permissionGroupButtonList.stream().filter(t -> !containsButtonList.contains(t.getId())).map(ResourceModel::getId).collect(Collectors.toList());
        List<String> collect3 = authorizeList.stream().map(AuthorizeEntity::getItemId).collect(Collectors.toList());
        ids.addAll(collect3);
        ids.addAll(containsButtonList);
        noContainsIds.addAll(noContainsButtonList);
        // 我的菜单
        List<ModuleModel> myModuleList = authorizeModel.getModuleList();
        // 共有菜单
        List<String> containsModuleList = myModuleList.stream().filter(t -> moduleIdIds.contains(t.getId())).map(ModuleModel::getId).collect(Collectors.toList());
        List<String> myModuleIds = myModuleList.stream().map(ModuleModel::getId).collect(Collectors.toList());
        List<String> noContainsModuleList = moduleIdIds.stream().filter(t -> !myModuleIds.contains(t)).collect(Collectors.toList());
        ids.addAll(containsModuleList);
        noContainsIds.addAll(noContainsModuleList);
        // 我的应用
        List<SystemBaeModel> mySystemList = authorizeModel.getSystemList();
        // 共有应用
        List<String> containsSystemList = mySystemList.stream().filter(t -> systemIdIds.contains(t.getId())).map(SystemBaeModel::getId).collect(Collectors.toList());
        List<String> mySystemIds = mySystemList.stream().map(SystemBaeModel::getId).collect(Collectors.toList());
        List<String> noContainsSystemList = systemIdIds.stream().filter(t -> !mySystemIds.contains(t)).collect(Collectors.toList());
        ids.addAll(containsSystemList);
        noContainsIds.addAll(noContainsSystemList);
        // 所有按钮权限的上级
        List<String> allModuleIds = allButtonList.stream().map(ResourceModel::getParentId).distinct().collect(Collectors.toList());
        Map<String, ModuleEntity> allModuleListMap = new HashMap<>();
        // 新建APP菜单上级
        allModuleIds.forEach(t -> {
            ModuleEntity entity = moduleMap.get(t);
            while (entity != null) {
                allModuleListMap.put(entity.getId(), entity);
                entity = moduleMap.get(entity.getParentId());
            }
        });
        // 存放上级菜单id及上级 systemId,id
        Map<String, String> appIds = new HashMap<>(16);
        Map<String, String> webIds = new HashMap<>(16);
        long datetime = System.currentTimeMillis();
        allModuleListMap.values().stream().sorted(Comparator.comparing(ModuleEntity::getCategory).reversed()).forEach(t -> {
            if ("App".equals(t.getCategory()) && "-1".equals(t.getParentId())) {
                if (!appIds.containsKey(t.getSystemId())) {
                    t.setParentId(t.getSystemId() + "1");
                    ResourceModel appData = new ResourceModel();
                    appData.setId(t.getSystemId() + "1");
                    appData.setSortCode(0L);
                    appData.setCreatorTime(datetime);
                    appData.setFullName("APP菜单");
                    appData.setIcon("icon-ym icon-ym-mobile");
                    appData.setParentId(t.getSystemId());
                    allButtonList.add(appData);
                    appIds.put(t.getSystemId(), appData.getId());
                } else {
                    t.setParentId(appIds.get(t.getSystemId()) + "");
                }
            }
            else if ("Web".equals(t.getCategory()) && "-1".equals(t.getParentId())) {
                if (!webIds.containsKey(t.getSystemId())) {
                    t.setParentId(t.getSystemId() + "2");
                    ResourceModel webData = new ResourceModel();
                    webData.setId(t.getSystemId() + "2");
                    webData.setSortCode(-1L);
                    webData.setCreatorTime(datetime);
                    webData.setFullName("WEB菜单");
                    webData.setIcon("icon-ym icon-ym-pc");
                    webData.setParentId(t.getSystemId());
                    allButtonList.add(webData);
                    webIds.put(t.getSystemId(), webData.getId());
                } else {
                    t.setParentId(webIds.get(t.getSystemId()) + "");
                }
            }
            ResourceModel model = JsonUtil.getJsonToBean(t, ResourceModel.class);
            allButtonList.add(model);
        });
        allButtonList.stream().filter(t -> "-1".equals(t.getParentId())).forEach(t -> t.setParentId(t.getSystemId()));
        List<String> systemId = allButtonList.stream().filter(t -> StringUtil.isNotEmpty(t.getParentId())).map(ResourceModel::getParentId).collect(Collectors.toList());
        systemId.forEach(t -> {
            if (Optional.ofNullable(systemEntityMap.get(t)).isPresent()) {
                ResourceModel jsonToBean = JsonUtil.getJsonToBean(systemEntityMap.get(t), ResourceModel.class);
                jsonToBean.setParentId("-1");
                allButtonList.add(jsonToBean);
            }
        });
        List<AuthorizeDataModel> treeList = JsonUtil.getJsonToList(allButtonList, AuthorizeDataModel.class);
        // 处理不可选的
        treeList.forEach(t -> {
            if (noContainsIds.contains(t.getId())) {
                t.setDisabled(true);
            }
        });
        treeList = treeList.stream().sorted(Comparator.comparing(AuthorizeDataModel::getSortCode).thenComparing(AuthorizeDataModel::getCreatorTime, Comparator.reverseOrder())).collect(Collectors.toList());
        List<SumTree<AuthorizeDataModel>> trees = TreeDotUtils.convertListToTreeDot(treeList);
        List<AuthorizeDataReturnModel> data = JsonUtil.getJsonToList(trees, AuthorizeDataReturnModel.class);
        vo.setAll(allButtonList.stream().map(ResourceModel::getId).distinct().collect(Collectors.toList()));
        vo.setList(data);
        ids.addAll(noContainsIds);
        vo.setIds(ids.stream().distinct().collect(Collectors.toList()));
        return vo;
    }

    /**
     * 角色信息
     *
     * @param data 数据
     * @return
     */
    AuthorizeDataReturnVO roleTree(List<RoleEntity> data) {
        List<AuthorizeDataModel> treeList = new ArrayList<>();
        List<DictionaryDataEntity> typeData = dictionaryDataApi.getList("4501f6f26a384757bce12d4c4b03342c");
        for (DictionaryDataEntity entity : typeData) {
            AuthorizeDataModel dictionary = new AuthorizeDataModel();
            dictionary.setId(entity.getEnCode());
            dictionary.setFullName(entity.getFullName());
            dictionary.setShowcheck(false);
            dictionary.setParentId("-1");
            treeList.add(dictionary);
        }
        for (RoleEntity entity : data) {
            AuthorizeDataModel role = new AuthorizeDataModel();
            role.setId(entity.getId());
            role.setFullName(entity.getFullName());
            role.setParentId(entity.getType());
            role.setShowcheck(true);
            role.setIcon("fa fa-umbrella");
            treeList.add(role);
        }
        AuthorizeDataReturnVO vo = new AuthorizeDataReturnVO();
        vo.setList(JsonUtil.getJsonToList(TreeDotUtils.convertListToTreeDot(treeList), AuthorizeDataReturnModel.class));
        return vo;
    }

    /**
     * 岗位信息
     *
     * @param organizeData 机构
     * @param positionData 岗位
     * @return
     */
    AuthorizeDataReturnVO positionTree(List<OrganizeEntity> organizeData, List<PositionEntity> positionData) {
        List<AuthorizeDataModel> treeList = new ArrayList<>();
        for (OrganizeEntity entity : organizeData) {
            AuthorizeDataModel organize = new AuthorizeDataModel();
            organize.setId(entity.getId());
            organize.setShowcheck(false);
            organize.setFullName(entity.getFullName());
            organize.setParentId(entity.getParentId());
            treeList.add(organize);
        }
        for (PositionEntity entity : positionData) {
            AuthorizeDataModel position = new AuthorizeDataModel();
            position.setId(entity.getId());
            position.setFullName(entity.getFullName());
            position.setTitle(entity.getEnCode());
            position.setParentId(entity.getOrganizeId());
            position.setShowcheck(true);
            position.setIcon("fa fa-briefcase");
            treeList.add(position);
        }
        AuthorizeDataReturnVO vo = new AuthorizeDataReturnVO();
        vo.setList(JsonUtil.getJsonToList(TreeDotUtils.convertListToTreeDot(treeList), AuthorizeDataReturnModel.class));
        return vo;
    }

    /**
     * 用户信息
     *
     * @param organizeData 机构
     * @param userData     用户
     * @return
     */
    private AuthorizeDataReturnVO userTree(List<OrganizeEntity> organizeData, List<UserEntity> userData) {
        List<AuthorizeDataModel> treeList = new ArrayList<>();
        for (OrganizeEntity entity : organizeData) {
            AuthorizeDataModel organize = new AuthorizeDataModel();
            organize.setId(entity.getId());
            organize.setShowcheck(false);
            organize.setFullName(entity.getFullName());
            organize.setParentId(entity.getParentId());
            treeList.add(organize);
        }
        for (UserEntity entity : userData) {
            AuthorizeDataModel user = new AuthorizeDataModel();
            user.setId(entity.getId());
            user.setFullName(entity.getRealName() + "/" + entity.getAccount());
            user.setParentId(entity.getOrganizeId());
            user.setShowcheck(true);
            user.setIcon("fa fa-user");
            treeList.add(user);
        }
        AuthorizeDataReturnVO vo = new AuthorizeDataReturnVO();
        vo.setList(JsonUtil.getJsonToList(TreeDotUtils.convertListToTreeDot(treeList), AuthorizeDataReturnModel.class));
        return vo;
    }

    //API调用接口，勿删！！！---------

    /**
     * 根据对象Id获取列表
     *
     * @param objectId 对象主键
     * @return
     */
    @Override
    @GetMapping("/GetListByObjectId/{objectId}")
    public List<AuthorizeEntity> getListByObjectId(@PathVariable("objectId") String objectId) {
        return authorizeService.getListByObjectId(Collections.singletonList(objectId));
    }

    /**
     * 根据对象Id获取列表
     *
     * @param objectId 对象主键
     * @param type     类型
     * @return
     */
    @Override
    @GetMapping("/GetListByObjectId/{objectId}/{type}")
    public List<AuthorizeEntity> getListByObjectId(@PathVariable("objectId") String objectId, @PathVariable("type") String type) {
        return authorizeService.getListByObjectId(objectId, type);
    }

    /**
     * 将查出来的某个对象删除
     *
     * @param queryWrapper
     * @return
     */
    @Override
    @DeleteMapping("/remove")
    public void remove(QueryWrapper<AuthorizeEntity> queryWrapper) {
        authorizeService.remove(queryWrapper);
    }

    @Override
    @GetMapping("/getAuthorize")
    public AuthorizeVO getAuthorize(@RequestParam("isCache") boolean isCache,@RequestParam("singletonOrg")  boolean singletonOrg) {
        return authorizeService.getAuthorize(isCache, singletonOrg);
    }

    @Override
    @PostMapping("/getConditionSql")
    public byte[] getConditionSql(@RequestBody AuthorizeConditionModel conditionModel) {

        return null;
    }

    @Override
    @PostMapping("/getCondition")
    public byte[] getCondition(@RequestBody AuthorizeConditionModel conditionModel) {
        return authorizeService.getCondition(conditionModel);
    }

//    @Override
//    @PostMapping("/findSystem")
//    public List<SystemBaeModel> findSystem(@RequestBody List<String> roleIds) {
//        List<String> roleId = new ArrayList<>(roleIds.size());
//        roleIds.forEach(t -> {
//            roleId.add("'" + t + "'");
//        });
//        String join = String.join(",", roleId);
//        return XSSEscape.escapeObj(authorizeMapper.findSystem(XSSEscape.escape(join), PlatformConst.MAIN_SYSTEM_CODE));
//    }

    @Override
    @GetMapping("/findButton")
    public List<ButtonModel> findButton(String objectId) {
        return authorizeService.findButton(objectId);
    }

    @Override
    @GetMapping("/findColumn")
    public List<ColumnModel> findColumn(String objectId) {
        return authorizeService.findColumn(objectId);
    }

    @Override
    @GetMapping("/findResource")
    public List<ResourceModel> findResource(String objectId) {
        return authorizeService.findResource(objectId);
    }

    @Override
    @GetMapping("/findForms")
    public List<ModuleFormModel> findForms(String objectId) {
        return authorizeService.findForms(objectId);
    }

    @Override
    @GetMapping("/findButtonAdmin")
    public List<ButtonModel> findButtonAdmin(Integer mark) {
        return authorizeService.findButtonAdmin(mark);
    }

    @Override
    @GetMapping("/findColumnAdmin")
    public List<ColumnModel> findColumnAdmin(Integer mark) {
        return authorizeService.findColumnAdmin(mark);
    }

    @Override
    @GetMapping("/findResourceAdmin")
    public List<ResourceModel> findResourceAdmin(Integer mark) {
        return authorizeService.findResourceAdmin(mark);
    }

    @Override
    @GetMapping("/findFormsAdmin")
    public List<ModuleFormModel> findFormsAdmin(Integer mark) {
        return authorizeService.findFormsAdmin(mark);
    }

    @Override
    @GetMapping("/getAuthorizeByItem")
    public List<AuthorizeEntity> getAuthorizeByItem(@RequestParam(value = "itemType", required = false) String itemType,
                                                    @RequestParam(value = "itemId", required = false) String itemId) {
        return authorizeService.getAuthorizeByItem(itemType, itemId);
    }

    @Override
    @GetMapping("/getAuthorizeByUser")
    public AuthorizeVO getAuthorizeByUser(@RequestParam("singletonOrg") boolean singletonOrg) {
        return authorizeService.getAuthorizeByUser(singletonOrg);
    }
}
