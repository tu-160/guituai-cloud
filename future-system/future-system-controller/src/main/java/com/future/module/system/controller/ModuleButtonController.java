package com.future.module.system.controller;


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
import com.future.common.constant.MsgCode;
import com.future.common.exception.DataException;
import com.future.common.util.JsonUtil;
import com.future.common.util.treeutil.SumTree;
import com.future.common.util.treeutil.TreeDotUtils;
import com.future.module.system.ModuleButtonApi;
import com.future.module.system.entity.ModuleButtonEntity;
import com.future.module.system.model.button.*;
import com.future.module.system.service.ModuleButtonService;

import java.util.List;

/**
 * 按钮权限
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2019年9月27日 上午9:18
 */
@Tag(name = "按钮权限", description = "ModuleButton")
@RestController
@RequestMapping("/ModuleButton")
public class ModuleButtonController extends SuperController<ModuleButtonService, ModuleButtonEntity> implements ModuleButtonApi {

    @Autowired
    private ModuleButtonService moduleButtonService;

    /**
     * 按钮按钮权限列表
     *
     * @param menuId 功能主键
     * @param pagination 分页模型
     * @return
     */
    @Operation(summary = "获取按钮权限列表")
    @Parameters({
            @Parameter(name = "menuId", description = "功能主键", required = true)
    })
    @SaCheckPermission("system.menu")
    @GetMapping("/{menuId}/List")
    public ActionResult list(@PathVariable("menuId") String menuId, Pagination pagination) {
        List<ModuleButtonEntity> data = moduleButtonService.getListByModuleIds(menuId, pagination);
        List<ButtonTreeListModel> treeList = JsonUtil.getJsonToList(data, ButtonTreeListModel.class);
        List<SumTree<ButtonTreeListModel>> sumTrees = TreeDotUtils.convertListToTreeDot(treeList);
        if (data.size() > sumTrees.size()) {
            List<ButtonTreeListVO> list = JsonUtil.getJsonToList(sumTrees, ButtonTreeListVO.class);
            ListVO<ButtonTreeListVO> treeVo = new ListVO<>();
            treeVo.setList(list);
            return ActionResult.success(treeVo);
        }
        List<ButtonListVO> list = JsonUtil.getJsonToList(treeList, ButtonListVO.class);
        ListVO<ButtonListVO> treeVo1 = new ListVO<>();
        treeVo1.setList(list);
        return ActionResult.success(treeVo1);
    }


    /**
     * 按钮按钮权限列表
     *
     * @param menuId 功能主键
     * @return
     */
    @Operation(summary = "获取按钮权限下拉框")
    @Parameters({
            @Parameter(name = "menuId", description = "功能主键", required = true)
    })
    @GetMapping("/{menuId}/Selector")
    public ActionResult<ListVO<ButtonTreeListVO>> selectList(@PathVariable("menuId") String menuId) {
        List<ModuleButtonEntity> data = moduleButtonService.getListByModuleIds(menuId);
        List<ButtonTreeListModel> treeList = JsonUtil.getJsonToList(data, ButtonTreeListModel.class);
        List<SumTree<ButtonTreeListModel>> sumTrees = TreeDotUtils.convertListToTreeDot(treeList);
        List<ButtonTreeListVO> list = JsonUtil.getJsonToList(sumTrees, ButtonTreeListVO.class);
        ListVO<ButtonTreeListVO> treeVo = new ListVO<>();
        treeVo.setList(list);
        return ActionResult.success(treeVo);
    }


    /**
     * 获取按钮权限信息
     *
     * @param id 主键值
     * @return
     */
    @Operation(summary = "获取按钮权限信息")
    @Parameters({
            @Parameter(name = "id", description = "主键值", required = true)
    })
    @SaCheckPermission("system.menu")
    @GetMapping("/{id}")
    public ActionResult<ModuleButtonInfoVO> info(@PathVariable("id") String id)throws DataException {
        ModuleButtonEntity entity = moduleButtonService.getInfo(id);
        ModuleButtonInfoVO vo = JsonUtil.getJsonToBeanEx(entity, ModuleButtonInfoVO.class);
        return ActionResult.success(vo);
    }

    /**
     * 新建按钮权限
     *
     * @param moduleButtonCrForm 实体对象
     * @return
     */
    @Operation(summary = "新建按钮权限")
    @Parameters({
            @Parameter(name = "moduleButtonCrForm", description = "实体对象", required = true)
    })
    @SaCheckPermission("system.menu")
    @PostMapping
    public ActionResult create(@RequestBody ModuleButtonCrForm moduleButtonCrForm) {
        ModuleButtonEntity entity = JsonUtil.getJsonToBean(moduleButtonCrForm, ModuleButtonEntity.class);
        if (moduleButtonService.isExistByEnCode(entity.getModuleId(), entity.getEnCode(), entity.getId())) {
            return ActionResult.fail(MsgCode.EXIST002.get());
        }
        moduleButtonService.create(entity);
        return ActionResult.success(MsgCode.SU001.get());
    }

    /**
     * 更新按钮权限
     *
     * @param id 主键值
     * @param moduleButtonUpForm 实体对象
     * @return
     */
    @Operation(summary = "更新按钮权限")
    @Parameters({
            @Parameter(name = "id", description = "主键值", required = true),
            @Parameter(name = "moduleButtonUpForm", description = "实体对象", required = true)
    })
    @SaCheckPermission("system.menu")
    @PutMapping("/{id}")
    public ActionResult update(@PathVariable("id") String id, @RequestBody ModuleButtonUpForm moduleButtonUpForm) {
        ModuleButtonEntity entity = JsonUtil.getJsonToBean(moduleButtonUpForm, ModuleButtonEntity.class);
        if (moduleButtonService.isExistByEnCode(entity.getModuleId(), entity.getEnCode(), id)) {
            return ActionResult.fail(MsgCode.EXIST002.get());
        }
        boolean flag = moduleButtonService.update(id, entity);
        if (flag == false) {
            return ActionResult.success(MsgCode.FA002.get());
        }
        return ActionResult.success(MsgCode.SU004.get());
    }

    /**
     * 删除按钮权限
     *
     * @param id 主键值
     * @return
     */
    @Operation(summary = "删除按钮权限")
    @Parameters({
            @Parameter(name = "id", description = "主键值", required = true)
    })
    @SaCheckPermission("system.menu")
    @DeleteMapping("/{id}")
    public ActionResult delete(@PathVariable("id") String id) {
        ModuleButtonEntity entity = moduleButtonService.getInfo(id);
        if (entity != null) {
            moduleButtonService.delete(entity);
            return ActionResult.success("删除成功");
        }
        return ActionResult.fail("删除失败，数据不存在");
    }

    /**
     * 更新菜单状态
     *
     * @param id 主键值
     * @return
     */
    @Operation(summary = "更新菜单状态")
    @Parameters({
            @Parameter(name = "id", description = "主键值", required = true)
    })
    @SaCheckPermission("system.menu")
    @PutMapping("/{id}/Actions/State")
    public ActionResult upState(@PathVariable("id") String id) {
        ModuleButtonEntity entity = moduleButtonService.getInfo(id);
        if (entity.getEnabledMark() == null || entity.getEnabledMark() == 1) {
            entity.setEnabledMark(0);
        } else {
            entity.setEnabledMark(1);
        }
       boolean flag= moduleButtonService.update(id, entity);
        if(flag==false){
            return ActionResult.success("更新失败，数据不存在");
        }
        return ActionResult.success("更新成功");
    }

    @Override
    @GetMapping("/getList")
    public List<ModuleButtonEntity> getList() {
        return moduleButtonService.getListByModuleIds();
    }

    @Override
    @PostMapping("/getListByModuleId")
    public List<ModuleButtonEntity> getListByModuleIds(@RequestBody List<String> ids) {
        return moduleButtonService.getListByModuleIds(ids);
    }

    @Override
    @PostMapping("/getListByIds")
    public List<ModuleButtonEntity> getListByIds(@RequestBody List<String> ids) {
        return moduleButtonService.getListByIds(ids);
    }
}
