package com.future.module.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.future.base.controller.SuperController;
import com.future.base.service.*;
import com.future.common.base.ActionResult;
import com.future.common.base.entity.*;
import com.future.common.base.vo.DownloadVO;
import com.future.common.base.vo.ListVO;
import com.future.common.constant.PlatformConst;
import com.future.common.constant.MsgCode;
import com.future.common.emnus.ModuleTypeEnum;
import com.future.common.exception.DataException;
import com.future.common.model.FlowWorkModel;
import com.future.common.model.UserMenuModel;
import com.future.common.model.tenant.*;
import com.future.common.util.*;
import com.future.common.util.treeutil.SumTree;
import com.future.common.util.treeutil.newtreeutil.TreeDotUtils;
import com.future.common.util.type.AuthorizeType;
import com.future.database.util.TenantDataSourceUtil;
import com.future.module.app.AppApi;
import com.future.module.app.entity.AppDataEntity;
import com.future.module.app.model.AppObjectDataModel;
import com.future.module.system.ModuleApi;
import com.future.module.system.entity.ModuleButtonEntity;
import com.future.module.system.entity.ModuleColumnEntity;
import com.future.module.system.entity.ModuleDataAuthorizeSchemeEntity;
import com.future.module.system.entity.ModuleEntity;
import com.future.module.system.entity.ModuleFormEntity;
import com.future.module.system.entity.SystemEntity;
import com.future.module.system.model.module.*;
import com.future.module.system.model.online.VisualMenuModel;
import com.future.module.system.service.ModuleButtonService;
import com.future.module.system.service.ModuleColumnService;
import com.future.module.system.service.ModuleDataAuthorizeSchemeService;
import com.future.module.system.service.ModuleFormService;
import com.future.module.system.service.ModuleService;
import com.future.module.system.service.SystemService;
import com.future.module.system.util.visualUtil.PubulishUtil;
import com.future.permission.AuthorizeApi;
import com.future.permission.PermissionGroupApi;
import com.future.permission.UserApi;
import com.future.permission.entity.AuthorizeEntity;
import com.future.permission.entity.PermissionGroupEntity;
import com.future.permission.model.user.UserIdListVo;
import com.future.reids.config.ConfigValueUtil;
import com.future.reids.util.RedisUtil;

import javax.validation.Valid;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 系统功能
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2019年9月27日 上午9:18
 */
@Tag(name = "系统菜单", description = "menu")
@RestController
@RequestMapping("/Menu")
public class ModuleController extends SuperController<ModuleService, ModuleEntity> implements ModuleApi {

    @Autowired
    private ModuleService moduleService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private CacheKeyUtil cacheKeyUtil;
    @Autowired
    private UserProvider userProvider;
    @Autowired
    private PubulishUtil pubulishUtil;
    @Autowired
    private SystemService systemService;
    @Autowired
    private AppApi appApi;
    @Autowired
    private PermissionGroupApi permissionGroupApi;
    @Autowired
    private ModuleButtonService buttonService;
    @Autowired
    private ModuleColumnService columnService;
    @Autowired
    private ModuleFormService formService;
    @Autowired
    private ModuleDataAuthorizeSchemeService dataAuthorizeSchemeService;
    @Autowired
    private UserApi userApi;
    @Autowired
    private AuthorizeApi authorizeApi;
    @Autowired
    private ConfigValueUtil configValueUtil;

    /**
     * 获取菜单列表
     *
     * @param systemId 系统id
     * @param paginationMenu 分页模型
     * @return
     */
    @Operation(summary = "获取菜单列表")
    @Parameters({
            @Parameter(name = "systemId", description = "系统id", required = true)
    })
    @GetMapping("/ModuleBySystem/{systemId}")
    public ActionResult<ListVO<MenuListVO>> list(@PathVariable("systemId") String systemId, PaginationMenu paginationMenu) {
        List<ModuleEntity> data = moduleService.getList(systemId, paginationMenu.getCategory(), paginationMenu.getKeyword(), paginationMenu.getType(), paginationMenu.getEnabledMark(), null, false);
        // 递归查上级
        Map<String, ModuleEntity> moduleEntityMap = data.stream().collect(Collectors.toMap(ModuleEntity::getId, Function.identity()));
        if(StringUtil.isNotEmpty(paginationMenu.getKeyword())) {
            moduleService.getParentModule(data, moduleEntityMap);
        }
        List<UserMenuModel> list = JsonUtil.getJsonToList(moduleEntityMap.values(), UserMenuModel.class);
        list = list.stream().sorted(Comparator.comparing(UserMenuModel::getSortCode, Comparator.nullsLast(Comparator.naturalOrder())).thenComparing(UserMenuModel::getCreatorTime, Comparator.nullsLast(Comparator.reverseOrder()))).collect(Collectors.toList());
        List<SumTree<UserMenuModel>> menuList = TreeDotUtils.convertListToTreeDot(list);
        List<MenuListVO> menuvo = JsonUtil.getJsonToList(menuList, MenuListVO.class);
        ListVO vo = new ListVO();
        vo.setList(menuvo);
        return ActionResult.success(vo);
    }

    /**
     * 获取菜单列表(下拉框)
     *
     * @param category 分类
     * @param id       主键
     * @return ignore
     */
    @Operation(summary = "获取菜单列表(下拉框)")
    @Parameters({
            @Parameter(name = "category", description = "分类"),
            @Parameter(name = "id", description = "主键", required = true)
    })
    @GetMapping("/Selector/{id}")
    public ActionResult<ListVO<MenuSelectVO>> treeView(@RequestParam(value = "category", required = false) String category, @PathVariable("id") String id) {
        String systemId = "App".equals(category) ? userProvider.get().getAppSystemId() : userProvider.get().getSystemId();
        List<ModuleEntity> data = moduleService.getList(systemId, category, null, 1, 1, null, false);
        if (!"0".equals(id)) {
            data.remove(moduleService.getInfo(id));
        }
        List<UserMenuModel> list = JsonUtil.getJsonToList(data, UserMenuModel.class);
        List<SumTree<UserMenuModel>> menuList = TreeDotUtils.convertListToTreeDotFilter(list);
        List<MenuSelectVO> menuvo = JsonUtil.getJsonToList(menuList, MenuSelectVO.class);
        ListVO vo = new ListVO();
        vo.setList(menuvo);
        return ActionResult.success(vo);
    }

    /**
     * 获取菜单权限返回权限组
     *
     * @param id       主键
     * @return ignore
     */
    @Operation(summary = "获取菜单权限返回权限组")
    @Parameters({
            @Parameter(name = "id", description = "主键", required = true)
    })
    @GetMapping("/getPermissionGroup/{id}")
    public ActionResult<Map<String, Object>> getPermissionGroup(@PathVariable("id") String id) {
        Map<String, Object> map = new HashMap<>(2);
        int type = 0; // 0未开启权限，1有
        List<FlowWorkModel> list = new ArrayList<>();
        // 获取当前菜单开启了哪些权限
        ModuleEntity entity = moduleService.getInfo(id);
        if (entity == null) {
            return ActionResult.fail(MsgCode.FA001.get());
        }
        // 权限是否被绑定，是否有权限
        List<ModuleButtonEntity> buttonList = buttonService.getListByModuleIds(id);
        List<ModuleColumnEntity> columnList = columnService.getList(id);
        List<ModuleFormEntity> formList = formService.getList(id);
        List<ModuleDataAuthorizeSchemeEntity> dataAuthorizeList = dataAuthorizeSchemeService.getList(id);
        if (buttonList.size() > 0 || columnList.size() > 0 || formList.size() >0 || dataAuthorizeList.size() > 0) {
            List<AuthorizeEntity> authorizeByItem = authorizeApi.getAuthorizeByItem(AuthorizeType.MODULE, entity.getId());
            List<String> collect = authorizeByItem.stream().map(AuthorizeEntity::getObjectId).collect(Collectors.toList());
            List<PermissionGroupEntity> permissionGroupByUserId = permissionGroupApi.getListByIds(collect);
            list = JsonUtil.getJsonToList(permissionGroupByUserId, FlowWorkModel.class);
            list.forEach(t -> t.setIcon("icon-ym icon-ym-authGroup"));
            if (list.size() > 0) {
                type = 1;
            } else {
                type = 2;
            }
        }
        map.put("list", list);
        map.put("type", type);
        return ActionResult.success(map);
    }

    /**
     * 通过权限组id获取相关权限
     *
     * @param id       主键
     * @return ignore
     */
    @Operation(summary = "通过权限组id获取相关权限")
    @Parameters({
            @Parameter(name = "id", description = "权限组id", required = true)
    })
    @GetMapping("/getPermission/{id}/{permissionId}")
    public ActionResult<ModulePermissionVO> getPermission(@PathVariable("id") String id, @PathVariable("permissionId") String permissionId) {
        // 获取当前菜单开启了哪些权限
        ModuleEntity entity = moduleService.getInfo(id);
        if (entity == null) {
            return ActionResult.fail(MsgCode.FA001.get());
        }
        PermissionGroupEntity permissionGroupEntity = permissionGroupApi.getInfoById(permissionId);
        if (permissionGroupEntity == null) {
            return ActionResult.fail(MsgCode.FA001.get());
        }
        // 权限组的权限
        List<AuthorizeEntity> authList = authorizeApi.getListByObjectId(permissionId);

        ModulePermissionVO modulePermissionVO = new ModulePermissionVO();

        ModulePermissionModel permissionMemberModel = new ModulePermissionModel();
        permissionMemberModel.setFullName("权限成员");
        permissionMemberModel.setType(1);
        List<ModulePermissionModel.ModulePermissionBaseModel> permissionMember = new ArrayList<>();
        if (StringUtil.isNotEmpty(permissionGroupEntity.getPermissionMember())) {
            List<UserIdListVo> userIdListVos = userApi.selectedByIds(Arrays.asList(permissionGroupEntity.getPermissionMember().split(",")));
            permissionMember = JsonUtil.getJsonToList(userIdListVos, ModulePermissionModel.ModulePermissionBaseModel.class);
        }
        permissionMemberModel.setList(permissionMember);
        modulePermissionVO.setPermissionMember(permissionMemberModel);


        ModulePermissionModel moduleButtonPermissionModel = new ModulePermissionModel();
        moduleButtonPermissionModel.setFullName("按钮权限");
        List<ModuleButtonEntity> buttonList = buttonService.getListByModuleIds(id);
        if (buttonList.size() > 0) {
            int type = 2;
            List<ModuleButtonEntity> collect = buttonList.stream()
                    .filter(t -> authList.stream().map(AuthorizeEntity::getItemId).collect(Collectors.toList())
                            .contains(t.getId())).collect(Collectors.toList());
            List<ModulePermissionModel.ModulePermissionBaseModel> buttonPermissionList = JsonUtil.getJsonToList(collect, ModulePermissionModel.ModulePermissionBaseModel.class);
            moduleButtonPermissionModel.setList(buttonPermissionList);
            if (buttonPermissionList.size() > 0) {
                type = 1;
            }
            moduleButtonPermissionModel.setType(type);
        }
        modulePermissionVO.setButtonAuthorize(moduleButtonPermissionModel);

        ModulePermissionModel moduleColumnPermissionModel = new ModulePermissionModel();
        moduleColumnPermissionModel.setFullName("列表权限");
        List<ModuleColumnEntity> columnList = columnService.getList(id);
        if (columnList.size() > 0) {
            int type = 2;
            List<ModuleColumnEntity> collect = columnList.stream()
                    .filter(t -> authList.stream().map(AuthorizeEntity::getItemId).collect(Collectors.toList())
                    .contains(t.getId())).collect(Collectors.toList());
            List<ModulePermissionModel.ModulePermissionBaseModel> buttonPermissionList = JsonUtil.getJsonToList(collect, ModulePermissionModel.ModulePermissionBaseModel.class);
            moduleColumnPermissionModel.setList(buttonPermissionList);
            if (buttonPermissionList.size() > 0) {
                type = 1;
            }
            moduleColumnPermissionModel.setType(type);
        }
        modulePermissionVO.setColumnAuthorize(moduleColumnPermissionModel);

        ModulePermissionModel moduleFromPermissionModel = new ModulePermissionModel();
        moduleFromPermissionModel.setFullName("表单权限");
        List<ModuleFormEntity> formList = formService.getList(id);
        if (formList.size() > 0) {
            int type = 2;
            List<ModuleFormEntity> collect = formList.stream()
                    .filter(t -> authList.stream().map(AuthorizeEntity::getItemId).collect(Collectors.toList())
                            .contains(t.getId())).collect(Collectors.toList());
            List<ModulePermissionModel.ModulePermissionBaseModel> buttonPermissionList = JsonUtil.getJsonToList(collect, ModulePermissionModel.ModulePermissionBaseModel.class);
            moduleFromPermissionModel.setList(buttonPermissionList);
            if (buttonPermissionList.size() > 0) {
                type = 1;
            }
            moduleFromPermissionModel.setType(type);
        }
        modulePermissionVO.setFormAuthorize(moduleFromPermissionModel);

        ModulePermissionModel moduleDataPermissionModel = new ModulePermissionModel();
        moduleDataPermissionModel.setFullName("数据权限");
        List<ModuleDataAuthorizeSchemeEntity> dataAuthorizeList = dataAuthorizeSchemeService.getList(id);
        if (dataAuthorizeList.size() > 0) {
            int type = 2;
            List<ModuleDataAuthorizeSchemeEntity> collect = dataAuthorizeList.stream()
                    .filter(t -> authList.stream().map(AuthorizeEntity::getItemId).collect(Collectors.toList())
                            .contains(t.getId())).collect(Collectors.toList());
            List<ModulePermissionModel.ModulePermissionBaseModel> buttonPermissionList = JsonUtil.getJsonToList(collect, ModulePermissionModel.ModulePermissionBaseModel.class);
            moduleDataPermissionModel.setList(buttonPermissionList);
            if (buttonPermissionList.size() > 0) {
                type = 1;
            }
            moduleDataPermissionModel.setType(type);
        }
        modulePermissionVO.setDataAuthorize(moduleDataPermissionModel);

        return ActionResult.success(modulePermissionVO);
    }

    /**
     * 获取开发平台菜单
     *
     * @return ignore
     */
    @Operation(summary = "获取开发平台菜单")
    @GetMapping("/SystemSelector")
    public ActionResult<ListVO<MenuSelectVO>> mainSystemSelector() {
        SystemEntity mainSystem = systemService.getInfoByEnCode(PlatformConst.MAIN_SYSTEM_CODE);
        List<ModuleEntity> data = moduleService.getList(mainSystem.getId(), null, null, null, 1, null, false);
        List<UserMenuModel> list = JsonUtil.getJsonToList(data, UserMenuModel.class);
            list.forEach(t -> {
                if ("-1".equals(t.getParentId())) {
                    t.setParentId(t.getSystemId());
                }
            });
        UserMenuModel userMenuModel = JsonUtil.getJsonToBean(mainSystem, UserMenuModel.class);
        userMenuModel.setType(0);
        userMenuModel.setParentId("-1");
        list.add(userMenuModel);
        List<SumTree<UserMenuModel>> menuList = TreeDotUtils.convertListToTreeDotFilter(list);
        List<MenuSelectVO> menuvo = JsonUtil.getJsonToList(menuList, MenuSelectVO.class);
        ListVO vo = new ListVO();
        vo.setList(menuvo);
        return ActionResult.success(vo);
    }

    /**
     * 通过系统id获取菜单列表(下拉框)
     *
     * @param category 分类
     * @param id       主键
     * @param systemId       系统主键
     * @return ignore
     */
    @Operation(summary = "通过系统id获取菜单列表(下拉框)")
    @Parameters({
            @Parameter(name = "id", description = "主键", required = true),
            @Parameter(name = "systemId", description = "系统主键", required = true)
    })
    @GetMapping("/Selector/{id}/{systemId}")
    public ActionResult<ListVO<MenuSelectAllVO>> treeView(String category, @PathVariable("id") String id, @PathVariable("systemId") String systemId) {
        List<ModuleEntity> data = moduleService.getList(systemId, category, null, 1, 1, null, true);
        if (!"0".equals(id)) {
            data.remove(moduleService.getInfo(id));
        }
        List<UserMenuModel> list = JsonUtil.getJsonToList(data, UserMenuModel.class);
        if ("0".equals(systemId)) {
            List<String> moduleAuthorize = new ArrayList<>();
            if (configValueUtil.isMultiTenancy()) {
                TenantAuthorizeModel tenantAuthorizeModel = TenantDataSourceUtil.getCacheModuleAuthorize(UserProvider.getUser().getTenantId());
                moduleAuthorize = tenantAuthorizeModel.getModuleIdList();
            }
            List<SystemEntity> list1 = systemService.getList(null, true, false, true, false, moduleAuthorize);
            list.forEach(t -> {
                if ("-1".equals(t.getParentId())) {
                    t.setParentId(t.getSystemId());
                }
            });
            List<UserMenuModel> jsonToList = JsonUtil.getJsonToList(list1, UserMenuModel.class);
            jsonToList.forEach(t -> {
                t.setType(0);
                t.setParentId("-1");
            });
            list.addAll(jsonToList);
        }
        List<SumTree<UserMenuModel>> menuList = TreeDotUtils.convertListToTreeDotFilter(list);
        List<MenuSelectAllVO> menuvo = JsonUtil.getJsonToList(menuList, MenuSelectAllVO.class);
        ListVO vo = new ListVO();
        vo.setList(menuvo);
        return ActionResult.success(vo);
    }

    /**
     * 获取菜单列表(下拉框)
     *
     * @param category 分类
     * @return ignore
     */
    @Operation(summary = "获取菜单列表下拉框")
    @Parameters({
            @Parameter(name = "category", description = "分类")
    })
    @GetMapping("/Selector/All")
    public ActionResult<ListVO<MenuSelectAllVO>> menuSelect(@RequestParam(value = "category", required = false) String category) {
        List<ModuleEntity> data = moduleService.getList("0", category, null, null, 1, null, false);
        List<UserMenuModel> list = JsonUtil.getJsonToList(data, UserMenuModel.class);
        List<String> moduleAuthorize = new ArrayList<>();
        if (configValueUtil.isMultiTenancy()) {
            TenantAuthorizeModel tenantAuthorizeModel = TenantDataSourceUtil.getCacheModuleAuthorize(UserProvider.getUser().getTenantId());
            moduleAuthorize = tenantAuthorizeModel.getModuleIdList();
        }
        List<SystemEntity> list1 = systemService.getList(null, true, false, false, false, moduleAuthorize);
        list.forEach(t -> {
            t.setHasModule(!"1".equals(String.valueOf(t.getType())));
            if ("-1".equals(t.getParentId())) {
                t.setParentId(t.getSystemId());
            }
        });
        List<UserMenuModel> jsonToList = JsonUtil.getJsonToList(list1, UserMenuModel.class);
        jsonToList.forEach(t -> {
            t.setType(0);
            t.setHasModule(false);
            t.setParentId("-1");
        });
        list.addAll(jsonToList);
        List<SumTree<UserMenuModel>> menuList = TreeDotUtils.convertListToTreeDotFilter(list);
        List<MenuSelectAllVO> menuvo = JsonUtil.getJsonToList(menuList, MenuSelectAllVO.class);
        ListVO vo = new ListVO();
        vo.setList(menuvo);
        return ActionResult.success(vo);
    }


    /**
     * 获取菜单信息
     *
     * @param id 主键值
     * @return ignore
     * @throws DataException ignore
     */
    @Operation(summary = "获取菜单信息")
    @Parameters({
            @Parameter(name = "id", description = "主键值", required = true)
    })
    @SaCheckPermission("system.menu")
    @GetMapping("/{id}")
    public ActionResult<ModuleInfoVO> info(@PathVariable("id") String id) throws DataException {
        ModuleEntity entity = moduleService.getInfo(id);
        ModuleInfoVO vo = JsonUtilEx.getJsonToBeanEx(entity, ModuleInfoVO.class);
        return ActionResult.success(vo);
    }


    /**
     * 新建系统功能
     *
     * @param moduleCrForm 实体对象
     * @return ignore
     */
    @Operation(summary = "新建系统功能")
    @Parameters({
            @Parameter(name = "moduleCrForm", description = "实体对象", required = true)
    })
    @SaCheckPermission("system.menu")
    @PostMapping
    public ActionResult create(@RequestBody @Valid ModuleCrForm moduleCrForm) {
        ModuleEntity entity = JsonUtil.getJsonToBean(moduleCrForm, ModuleEntity.class);
        if (entity.getUrlAddress() != null) {
            entity.setUrlAddress(entity.getUrlAddress().trim());
        }
        if (moduleService.isExistByFullName(entity, moduleCrForm.getCategory(), moduleCrForm.getSystemId())) {
            return ActionResult.fail(MsgCode.EXIST101.get());
        }
        if (moduleService.isExistByEnCode(entity, moduleCrForm.getCategory(), moduleCrForm.getSystemId())) {
            return ActionResult.fail(MsgCode.EXIST102.get());
        }
        moduleService.create(entity);
        return ActionResult.success(MsgCode.SU001.get());
    }

    /**
     * 更新系统功能
     *
     * @param id           主键值
     * @param moduleUpForm 实体对象
     * @return ignore
     */
    @Operation(summary = "更新系统功能")
    @Parameters({
            @Parameter(name = "id", description = "主键值", required = true),
            @Parameter(name = "moduleUpForm", description = "实体对象", required = true)
    })
    @SaCheckPermission("system.menu")
    @PutMapping("/{id}")
    public ActionResult update(@PathVariable("id") String id, @RequestBody @Valid ModuleUpForm moduleUpForm) {
        ModuleEntity entity = JsonUtil.getJsonToBean(moduleUpForm, ModuleEntity.class);
        //判断如果是目录则不能修改类型
        ModuleEntity moduleEntity = moduleService.getInfo(id);
        if (moduleEntity != null && moduleEntity.getType() == 1 && entity.getType() != 1 && moduleService.getList(moduleEntity.getId()).size() > 0) {
            return ActionResult.fail("当前目录存在数据,不能修改类型");
        }
        entity.setId(id);
        AppDataEntity appDataEntity = null;
        if (entity.getUrlAddress() != null) {
            entity.setUrlAddress(entity.getUrlAddress().trim());
            String urlAddress = "";
            appDataEntity = appApi.getData(id);
            if (appDataEntity != null && StringUtil.isNotEmpty(appDataEntity.getObjectData())) {
                AppObjectDataModel objectDataModel = JsonUtil.getJsonToBean(appDataEntity.getObjectData(), AppObjectDataModel.class);
                if (StringUtil.isNotEmpty(objectDataModel.getUrlAddress())) {
                    urlAddress = appDataEntity.getObjectData().replaceAll(objectDataModel.getUrlAddress(), entity.getUrlAddress());
                }
                appDataEntity.setObjectData(urlAddress);
            }
        }
        if (moduleService.isExistByFullName(entity, moduleUpForm.getCategory(), moduleUpForm.getSystemId())) {
            return ActionResult.fail(MsgCode.EXIST101.get());
        }
        if (moduleService.isExistByEnCode(entity, moduleUpForm.getCategory(), moduleUpForm.getSystemId())) {
            return ActionResult.fail(MsgCode.EXIST102.get());
        }
        boolean flag = moduleService.update(id, entity);
        if (!flag) {
            return ActionResult.fail(MsgCode.FA002.get());
        }
        // 修改常用应用数据
        if (appDataEntity != null) {
            appApi.updateData(appDataEntity);
        }
        return ActionResult.success(MsgCode.SU004.get());
    }

    /**
     * 删除系统功能
     *
     * @param id 主键值
     * @return ignore
     */
    @Operation(summary = "删除系统功能")
    @Parameters({
            @Parameter(name = "id", description = "主键值", required = true)
    })
    @SaCheckPermission("system.menu")
    @DeleteMapping("/{id}")
    public ActionResult delete(@PathVariable("id") String id) {
        ModuleEntity entity = moduleService.getInfo(id);
        if (entity != null) {
            List<ModuleEntity> list = moduleService.getList(false, new ArrayList<>(), new ArrayList<>()).stream().filter(t -> t.getParentId().equals(entity.getId())).collect(Collectors.toList());
            if (list.size() > 0) {
                return ActionResult.fail("删除失败，请先删除子菜单。");
            }
            moduleService.delete(entity);
            return ActionResult.success(MsgCode.SU003.get());
        }
        return ActionResult.fail(MsgCode.FA003.get());
    }

    /**
     * 更新菜单状态
     *
     * @param id 主键值
     * @return ignore
     */
    @Operation(summary = "更新菜单状态")
    @Parameters({
            @Parameter(name = "id", description = "主键值", required = true)
    })
    @SaCheckPermission("system.menu")
    @PutMapping("/{id}/Actions/State")
    public ActionResult upState(@PathVariable("id") String id) {
        ModuleEntity entity = moduleService.getInfo(id);
        if (entity != null) {
            if (entity.getEnabledMark() == null || "1".equals(String.valueOf(entity.getEnabledMark()))) {
                entity.setEnabledMark(0);
            } else {
                entity.setEnabledMark(1);
            }
            moduleService.update(id, entity);
            //清除redis权限
            String cacheKey = cacheKeyUtil.getUserAuthorize() + userProvider.get().getUserId();
            if (redisUtil.exists(cacheKey)) {
                redisUtil.remove(cacheKey);
            }
            return ActionResult.success(MsgCode.SU004.get());
        }
        return ActionResult.fail(MsgCode.FA002.get());
    }

    /**
     * 系统菜单导出功能
     *
     * @param id 接口id
     * @return ignore
     */
    @Operation(summary = "导出系统菜单数据")
    @Parameters({
            @Parameter(name = "id", description = "主键值", required = true)
    })
    @SaCheckPermission("system.menu")
    @GetMapping("/{id}/Actions/Export")
    public ActionResult exportFile(@PathVariable("id") String id) {
        DownloadVO downloadVO = moduleService.exportData(id);
        return ActionResult.success(downloadVO);
    }

    /**
     * 系统菜单导入功能
     *
     * @param systemId 系统id
     * @param multipartFile 文件
     * @param parentId 父级id
     * @param category 分类
     * @return
     * @throws DataException
     */
    @Operation(summary = "系统菜单导入功能")
    @Parameters({
            @Parameter(name = "systemId", description = "系统id", required = true),
//            @Parameter(name = "parentId", description = "父级id", required = true),
//            @Parameter(name = "category", description = "分类", required = true)
    })
    @SaCheckPermission("system.menu")
    @PostMapping(value = "/{systemId}/Actions/Import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ActionResult importFile(@PathVariable("systemId") String systemId,
                                   @RequestPart("file") MultipartFile multipartFile,
                                   @RequestParam("parentId") String parentId,
                                   @RequestParam("category") String category,
                                   @RequestParam("type") Integer type) throws DataException {
        //判断是否为.bm结尾
        if (FileUtil.existsSuffix(multipartFile, ModuleTypeEnum.SYSTEM_MODULE.getTableName())) {
            return ActionResult.fail(MsgCode.IMP002.get());
        }
        try {
            //读取文件内容
            String fileContent = FileUtil.getFileContent(multipartFile);
            //转model后导入
            ModuleExportModel exportModel = JsonUtil.getJsonToBean(fileContent, ModuleExportModel.class);
            ModuleEntity moduleEntity = exportModel.getModuleEntity();
            if (!category.equals(moduleEntity.getCategory())) {
                return ActionResult.fail("当前导入菜单为" + category.toUpperCase() + "端菜单，请在对应模块下导入！");
            }
            if ("App".equals(moduleEntity.getCategory()) && "-1".equals(parentId)) {
                return ActionResult.fail("请在顶级节点下创建目录后再进行菜单导入");
            }
            // 设置系统id然后重新赋值
            moduleEntity.setSystemId(systemId);
            moduleEntity.setParentId(parentId);
            //清空同步菜单记录 避免重复
            moduleEntity.setModuleId(null);
//            String enCode = moduleEntity.getEnCode();
//            moduleEntity.setEnCode(moduleEntity.getEnCode() + "_" + RandomUtil.getRandomCode());
//            if (moduleEntity.getType() == 3) {
//                moduleEntity.setUrlAddress(moduleEntity.getUrlAddress().replace(enCode, moduleEntity.getEnCode()));
//            }
            exportModel.setModuleEntity(moduleEntity);
            return moduleService.importData(exportModel, type);
        } catch (Exception e) {
            throw new DataException(MsgCode.IMP004.get());
        }
    }

    // ------------------多租户调用
    /**
     * 通过租户id获取菜单
     *
     * @param tenantMenuModel 模型
     * @return ignore
     */
    @Operation(summary = "通过租户id获取菜单")
    @Parameters({
            @Parameter(name = "tenantMenuModel", description = "模型", required = true)
    })
    @NoDataSourceBind
    @PostMapping("/Tenant/Menu")
    public TenantMenuVO menu(@RequestBody TenantMenuModel tenantMenuModel) throws DataException {
        if (configValueUtil.isMultiTenancy()) {
            TenantDataSourceUtil.switchTenant(tenantMenuModel.getTenantId());
        }
        List<SystemEntity> systemEntityList = systemService.getList();
        List<String> ids = new ArrayList<>();
        if (Objects.nonNull(tenantMenuModel.getIds())) {
            ids = tenantMenuModel.getIds();
        }
        List<String> urlAddressList = new ArrayList<>();
        if (Objects.nonNull(tenantMenuModel.getIds())) {
            urlAddressList = tenantMenuModel.getUrlAddressList();
        }
        List<ModuleEntity> moduleEntityList = moduleService.getList(true, new ArrayList<>(), new ArrayList<>());
        TenantMenuVO module = module(systemEntityList, moduleEntityList, ids, urlAddressList);
        return module;
    }

    /**
     * 功能权限
     *
     * @param moduleEntityList  所有菜单
     * @param systemEntityList  所有应用
     * @return
     */
    private TenantMenuVO module(List<SystemEntity> systemEntityList, List<ModuleEntity> moduleEntityList, List<String> ids, List<String> urlAddressList) {
        TenantMenuVO vo = new TenantMenuVO();
        // 转树前所有数据
        List<TenantMenuTreeModel> moduleAllList = new ArrayList<>();
        List<TenantMenuTreeModel> systemList = JsonUtil.getJsonToList(systemEntityList, TenantMenuTreeModel.class);
        systemList.forEach(t -> t.setParentId("-1"));
        moduleAllList.addAll(systemList);
        Map<String, List<ModuleEntity>> moduleMap = moduleEntityList.stream().collect(Collectors.groupingBy(t -> {
            if ("Web".equals(t.getCategory())) {
                return "Web";
            } else {
                return "App";
            }
        }));
        List<ModuleEntity> webModuleList = moduleMap.get("Web") == null ? new ArrayList<>() : moduleMap.get("Web");
        List<ModuleEntity> appModuleList = moduleMap.get("App") == null ? new ArrayList<>() : moduleMap.get("App");
        Map<String, ModuleEntity> appModuleMap = appModuleList.stream().collect(Collectors.toMap(ModuleEntity::getId, Function.identity()));
        Map<String, String> webIds = new HashMap<>(16);
        List<ModuleEntity> temWebList = webModuleList.stream().filter(t -> "-1".equals(t.getParentId())).collect(Collectors.toList());
        temWebList.stream().filter(t -> "-1".equals(t.getParentId())).forEach(t -> {
            if (!webIds.containsKey(t.getSystemId())) {
                ModuleEntity webData = new ModuleEntity();
                webData.setId(t.getSystemId() + "1");
                t.setParentId(webData.getId());
                webData.setFullName("WEB菜单");
                webData.setIcon("icon-ym icon-ym-pc");
                webData.setParentId(t.getSystemId());
                webData.setSystemId(t.getSystemId());
                webModuleList.add(webData);
                webIds.put(t.getSystemId(), webData.getId());
            } else {
                t.setParentId(webIds.get(t.getSystemId()) + "");
            }
        });
        List<TenantMenuTreeModel> webReturnModuleList = JsonUtil.getJsonToList(webModuleList, TenantMenuTreeModel.class);
        moduleAllList.addAll(webReturnModuleList);
        // 处理App菜单
        List<ModuleEntity> temList = appModuleList.stream().filter(t -> "-1".equals(t.getParentId()) && !PlatformConst.MAIN_SYSTEM_CODE.equals(t.getEnCode())).collect(Collectors.toList());
        Map<String, String> appIds = new HashMap<>(16);
        for (ModuleEntity appModuleEntity : temList) {
            if (StringUtil.isEmpty(appIds.get(appModuleEntity.getSystemId()))) {
                ModuleEntity appData = new ModuleEntity();
                appData.setId(appModuleEntity.getSystemId() + appModuleEntity.getSystemId() + "2");
                appModuleEntity.setParentId(appData.getId());
                appData.setFullName("APP菜单");
                appData.setIcon("icon-ym icon-ym-mobile");
                appData.setParentId(appModuleEntity.getSystemId());
                appData.setSystemId(appModuleEntity.getSystemId());
                appModuleList.add(appData);
                appIds.put(appModuleEntity.getSystemId(), appData.getId());
            } else {
                appModuleList.remove(appModuleEntity);
                ModuleEntity entity = appModuleMap.get(appModuleEntity.getId());
                entity.setParentId(appIds.get(appModuleEntity.getSystemId()));
                appModuleList.add(entity);
            }
        }
        List<TenantMenuTreeModel> appReturnModuleList = JsonUtil.getJsonToList(appModuleList, TenantMenuTreeModel.class);
        moduleAllList.addAll(appReturnModuleList);
        List<SumTree<TenantMenuTreeModel>> sumTrees = TreeDotUtils.convertListToTreeDot(moduleAllList);
        List<TenantMenuTreeReturnModel> data = new ArrayList<>();
        TenantMenuTreeReturnModel workFlowEnabled = new TenantMenuTreeReturnModel();
        workFlowEnabled.setId("-999");
        workFlowEnabled.setFullName("协同办公");
        data.add(workFlowEnabled);
        data.addAll(JsonUtil.getJsonToList(sumTrees, TenantMenuTreeReturnModel.class));
        vo.setList(data);
        List<String> allId = moduleAllList.stream().map(TenantMenuTreeModel::getId).collect(Collectors.toList());
        allId.add(workFlowEnabled.getId());
        vo.setAll(allId);
        List<String> ids0 = moduleService.getListByUrlAddress(ids, urlAddressList).stream().map(ModuleEntity::getId).collect(Collectors.toList());
        List<String> selectorIds = allId.stream().filter(t -> !ids0.contains(t)).collect(Collectors.toList());
        if (ids.contains("-999")) {
            selectorIds.remove("-999");
        }
        vo.setIds(selectorIds);
        return vo;
    }

    /**
     * 通过租户id及菜单id获取菜单
     *
     * @param tenantMenuModel 模型
     * @return ignore
     */
    @Operation(summary = "通过租户id及菜单id获取菜单")
    @Parameters({
            @Parameter(name = "tenantMenuModel", description = "模型", required = true)
    })
    @NoDataSourceBind
    @PostMapping("/Tenant/MenuByIds")
    public Map MenuByIds(@RequestBody TenantMenuModel tenantMenuModel) throws DataException {
        if (configValueUtil.isMultiTenancy()) {
            TenantDataSourceUtil.switchTenant(tenantMenuModel.getTenantId());
        }
        List<ModuleEntity> list = moduleService.getList();
        return list.stream().collect(Collectors.toMap(ModuleEntity::getId, ModuleEntity::getUrlAddress));
    }


    /**
     * 列表
     *
     * @return
     * @param model
     */
    @Override
    @PostMapping("/getList")
    public List<ModuleEntity> getList(@RequestBody ModuleApiModel model) {
        List<ModuleEntity> list = moduleService.getList(model.getFilterFlowWork(), model.getModuleAuthorize(), model.getModuleUrlAddressAuthorize());
        return list;
    }

    @Override
    @GetMapping("/getListById/{id}")
    public List<ModuleEntity> getList(@PathVariable("id") String id) {
        List<ModuleEntity> list = moduleService.getList(id);
        return list;
    }

    @Override
    @GetMapping("/getListByModuleId/{ModuleId}")
    public List<ModuleEntity> getModuleList(@PathVariable("ModuleId") String ModuleId){
        return moduleService.getModuleList(ModuleId);
    }

    @Override
    @GetMapping("/getInfoById")
    public ModuleEntity getModuleByList(@RequestParam("ModuleId") String moduleId) {
        return moduleService.getInfo(moduleId);
    }

    @Override
    @PostMapping("/pubulishToSys")
    @GlobalTransactional
    public Integer pubulish(@RequestBody(required = false) VisualMenuModel visualMenuModel) {
        return pubulishUtil.publishMenu(visualMenuModel);
    }

    @Override
    @PostMapping("/getMainModule")
    public List<ModuleEntity> getMainModule(@RequestBody ModuleApiModel model) {
        return moduleService.getMainModule(model.getModuleAuthorize(), model.getModuleUrlAddressAuthorize(), model.getSingletonOrg());
    }

    @Override
    @PostMapping("/getModuleByIds")
    public List<ModuleEntity> getModuleByIds(@RequestBody ModuleApiByIdsModel model) {
        return moduleService.getModuleByIds(model.getIds(), model.getModuleAuthorize() , model.getModuleUrlAddressAuthorize(), model.getSingletonOrg());
    }

    @Override
    @PostMapping("/getModuleBySystemIds")
    public List<ModuleEntity> getModuleBySystemIds(@RequestBody ModuleApiByIdsModel model) {
        return moduleService.getModuleBySystemIds(model.getIds(), model.getModuleAuthorize(), model.getModuleUrlAddressAuthorize());
    }

    @Override
    @PostMapping("/getListByEnCode")
    public List<ModuleEntity> getListByEnCode(@RequestBody List<String> enCodeList) {
        return moduleService.getListByEnCode(enCodeList);
    }

    @Override
    @PostMapping("/getModuleByPortal")
    public List<ModuleEntity> getModuleByPortal(@RequestBody List<String> portalIds) {
        return moduleService.getModuleByPortal(portalIds);
    }

    @Override
    @PostMapping("/findModuleAdmin")
    public List<ModuleEntity> findModuleAdmin(@RequestBody ModuleApiByIdAndMarkModel model) {
        return moduleService.findModuleAdmin(model.getMark(), model.getId(), model.getModuleAuthorize(), model.getModuleUrlAddressAuthorize());
    }

}
