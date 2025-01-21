package com.future.permission.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.future.base.controller.SuperController;
import com.future.common.base.ActionResult;
import com.future.common.base.Pagination;
import com.future.common.base.vo.ListVO;
import com.future.common.base.vo.PageListVO;
import com.future.common.base.vo.PaginationVO;
import com.future.common.constant.MsgCode;
import com.future.common.util.JsonUtil;
import com.future.common.util.enums.DictionaryDataEnum;
import com.future.common.util.treeutil.SumTree;
import com.future.common.util.treeutil.newtreeutil.TreeDotUtils;
import com.future.module.system.DictionaryDataApi;
import com.future.module.system.entity.DictionaryDataEntity;
import com.future.permission.GroupApi;
import com.future.permission.entity.GroupEntity;
import com.future.permission.model.user.UserIdModel;
import com.future.permission.model.usergroup.*;
import com.future.permission.service.GroupService;

import javax.validation.Valid;
import java.util.*;

/**
 * 分组管理控制器
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date ：2022/3/10 17:57
 */
@RestController
@Tag(name = "分组管理", description = "UserGroupController")
@RequestMapping("/Group")
public class GroupController extends SuperController<GroupService, GroupEntity> implements GroupApi {

    @Autowired
    private GroupService userGroupService;
    @Autowired
    private DictionaryDataApi dictionaryDataApi;

    /**
     * 获取分组管理列表
     *
     * @param pagination 分页模型
     * @return
     */
    @Operation(summary = "获取分组管理列表")
    @SaCheckPermission(value = {"permission.group"})
    @GetMapping
    public ActionResult<PageListVO<GroupPaginationVO>> list(PaginationGroup pagination) {
        List<GroupEntity> list = userGroupService.getList(pagination);
        List<GroupPaginationVO> jsonToList = JsonUtil.getJsonToList(list, GroupPaginationVO.class);
        // 通过数据字典获取类型
        List<DictionaryDataEntity> dictionaryDataEntities = dictionaryDataApi.getListByTypeDataCode(DictionaryDataEnum.PERMISSION_GROUP.getDictionaryTypeId()).getData();
        for (GroupPaginationVO userGroupPaginationVO : jsonToList) {
            DictionaryDataEntity dictionaryDataEntity = dictionaryDataEntities.stream().filter(t -> t.getId().equals(userGroupPaginationVO.getType())).findFirst().orElse(null);
            userGroupPaginationVO.setType(dictionaryDataEntity != null ? dictionaryDataEntity.getFullName() : userGroupPaginationVO.getId());
        }
        PaginationVO paginationVO = JsonUtil.getJsonToBean(pagination, PaginationVO.class);
        return ActionResult.page(jsonToList, paginationVO);
    }

    /**
     * 获取分组管理下拉框
     * @return
     */
    @Operation(summary = "获取分组管理下拉框")
    @GetMapping("/Selector")
    public ActionResult<List<GroupSelectorVO>> selector() {
        List<GroupTreeModel> tree = new ArrayList<>();
        List<GroupEntity> data = userGroupService.list();
        List<DictionaryDataEntity> dataEntityList = dictionaryDataApi.getListByTypeDataCode(DictionaryDataEnum.PERMISSION_GROUP.getDictionaryTypeId()).getData();
        // 获取分组管理外层菜单
        for (DictionaryDataEntity dictionaryDataEntity : dataEntityList) {
            GroupTreeModel firstModel = JsonUtil.getJsonToBean(dictionaryDataEntity, GroupTreeModel.class);
            firstModel.setId(dictionaryDataEntity.getId());
            firstModel.setType("0");
            long num = data.stream().filter(t -> t.getType().equals(dictionaryDataEntity.getId())).count();
            firstModel.setNum(num);
            if (num > 0) {
                tree.add(firstModel);
            }
        }
        for (GroupEntity entity : data) {
            GroupTreeModel treeModel = JsonUtil.getJsonToBean(entity, GroupTreeModel.class);
            treeModel.setType("group");
            treeModel.setParentId(entity.getType());
            treeModel.setIcon("icon-ym icon-ym-generator-group1");
            treeModel.setId(entity.getId());
            DictionaryDataEntity dataEntity = dictionaryDataApi.getInfo(entity.getType());
            if (dataEntity != null) {
                tree.add(treeModel);
            }
        }
        List<SumTree<GroupTreeModel>> sumTrees = TreeDotUtils.convertListToTreeDot(tree);
        List<GroupSelectorVO> list = JsonUtil.getJsonToList(sumTrees, GroupSelectorVO.class);
        ListVO<GroupSelectorVO> vo = new ListVO<>();
        vo.setList(list);
        return ActionResult.success(list);
    }

    /**
     * 自定义范围获取分组下拉框
     *
     * @param idModel 岗位选择模型
     * @return
     */
    @Operation(summary = "自定义范围获取分组下拉框")
    @Parameters({
            @Parameter(name = "positionConditionModel", description = "岗位选择模型", required = true)
    })
    @PostMapping("/GroupCondition")
    public ActionResult<ListVO<GroupSelectorVO>> positionCondition(@RequestBody UserIdModel idModel) {
        List<GroupEntity> data = userGroupService.getListByIds(idModel.getIds(), true);
        List<GroupTreeModel> tree = new ArrayList<>();
        List<DictionaryDataEntity> dataEntityList = dictionaryDataApi.getListByTypeDataCode(DictionaryDataEnum.PERMISSION_GROUP.getDictionaryTypeId()).getData();
        // 获取分组管理外层菜单
        for (DictionaryDataEntity dictionaryDataEntity : dataEntityList) {
            GroupTreeModel firstModel = JsonUtil.getJsonToBean(dictionaryDataEntity, GroupTreeModel.class);
            firstModel.setId(dictionaryDataEntity.getId());
            firstModel.setType("0");
            long num = data.stream().filter(t -> t.getType().equals(dictionaryDataEntity.getId())).count();
            firstModel.setNum(num);
            if (num > 0) {
                tree.add(firstModel);
            }
        }
        for (GroupEntity entity : data) {
            GroupTreeModel treeModel = JsonUtil.getJsonToBean(entity, GroupTreeModel.class);
            treeModel.setType("group");
            treeModel.setParentId(entity.getType());
            treeModel.setIcon("icon-ym icon-ym-generator-group1");
            treeModel.setId(entity.getId());
            DictionaryDataEntity dataEntity = dictionaryDataApi.getInfo(entity.getType());
            if (dataEntity != null) {
                tree.add(treeModel);
            }
        }
        List<SumTree<GroupTreeModel>> sumTrees = TreeDotUtils.convertListToTreeDot(tree);
        List<GroupSelectorVO> list = JsonUtil.getJsonToList(sumTrees, GroupSelectorVO.class);
        ListVO<GroupSelectorVO> vo = new ListVO<>();
        vo.setList(list);
        return ActionResult.success(vo);
    }

    /**
     * 信息
     *
     * @param id 主键
     * @return
     */
    @Operation(summary = "信息")
    @Parameters({
            @Parameter(name = "id", description = "主键", required = true)
    })
    @SaCheckPermission(value = {"permission.group"})
    @GetMapping("/{id}")
    public ActionResult<GroupInfoVO> info(@PathVariable("id") String id) {
        GroupEntity entity = userGroupService.getInfo(id);
        GroupInfoVO vo = JsonUtil.getJsonToBean(entity, GroupInfoVO.class);
        return ActionResult.success(vo);
    }

    /**
     * 创建
     *
     * @param userGroupCrForm 新建模型
     * @return
     */
    @Operation(summary = "创建")
    @Parameters({
            @Parameter(name = "userGroupCrForm", description = "新建模型", required = true)
    })
    @SaCheckPermission(value = {"permission.group"})
    @PostMapping
    public ActionResult create(@RequestBody @Valid GroupCrForm userGroupCrForm) {
        GroupEntity entity = JsonUtil.getJsonToBean(userGroupCrForm, GroupEntity.class);
        // 判断名称是否重复
        if (userGroupService.isExistByFullName(entity.getFullName(), entity.getId())) {
            return ActionResult.fail(MsgCode.EXIST001.get());
        }
        // 判断编码是否重复
        if (userGroupService.isExistByEnCode(entity.getEnCode(), entity.getId())) {
            return ActionResult.fail(MsgCode.EXIST002.get());
        }
        userGroupService.crete(entity);
        return ActionResult.success("创建成功");
    }

    /**
     * 更新
     *
     * @param id 主键
     * @param userGroupUpForm 修改模型
     * @return
     */
    @Operation(summary = "更新")
    @Parameters({
            @Parameter(name = "id", description = "主键", required = true),
            @Parameter(name = "userGroupUpForm", description = "修改模型", required = true)
    })
    @SaCheckPermission(value = {"permission.group"})
    @PutMapping("/{id}")
    public ActionResult update(@PathVariable("id") String id, @RequestBody @Valid GroupUpForm userGroupUpForm) {
        GroupEntity entity = JsonUtil.getJsonToBean(userGroupUpForm, GroupEntity.class);
        // 判断名称是否重复
        if (userGroupService.isExistByFullName(entity.getFullName(), id)) {
            return ActionResult.fail(MsgCode.EXIST001.get());
        }
        // 判断编码是否重复
        if (userGroupService.isExistByEnCode(entity.getEnCode(), id)) {
            return ActionResult.fail(MsgCode.EXIST002.get());
        }
        Boolean flag = userGroupService.update(id, entity);
        if (!flag) {
            return ActionResult.fail(MsgCode.FA013.get());
        }
        return ActionResult.success(MsgCode.SU004.get());
    }

    /**
     * 删除
     *
     * @param id 主键
     * @return
     */
    @Operation(summary = "删除")
    @Parameters({
            @Parameter(name = "id", description = "主键", required = true)
    })
    @SaCheckPermission(value = {"permission.group"})
    @DeleteMapping("/{id}")
    public ActionResult delete(@PathVariable("id") String id) {
        GroupEntity entity = userGroupService.getInfo(id);
        if (entity != null) {
            userGroupService.delete(entity);
            return ActionResult.success(MsgCode.SU003.get());
        }
        return ActionResult.fail(MsgCode.FA003.get());
    }

    /**
     * api调用
     * @param groupId
     * @return
     */
    @Override
    @GetMapping("/getInfoById/{groupId}")
    public GroupEntity getInfoById(@PathVariable("groupId") String groupId) {
        return userGroupService.getInfo(groupId);
    }

    @Override
    @GetMapping("/getGroupMap")
    public Map<String, Object> getGroupMap(@RequestParam("type") String type) {
        return "id-fullName".equals(type) ? userGroupService.getGroupMap() : userGroupService.getGroupEncodeMap();
    }

    @Override
    @PostMapping("/getGroupName")
    public List<GroupEntity> getGroupName(@RequestBody Map<String, Object> map) {
        boolean filterEnableMark = false;
        List<String> ids = (List<String>) map.get("ids");
        Object filterEnableMark1 = map.get("filterEnableMark");
        if (filterEnableMark1 != null) {
            filterEnableMark = Boolean.parseBoolean(filterEnableMark1.toString());
        }
        List<GroupEntity> list = userGroupService.getListByIds(ids, filterEnableMark);
        return list;
    }
}
