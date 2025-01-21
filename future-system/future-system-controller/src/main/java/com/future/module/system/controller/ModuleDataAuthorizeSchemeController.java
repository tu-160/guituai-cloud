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
import com.future.common.base.vo.ListVO;
import com.future.common.constant.MsgCode;
import com.future.common.exception.DataException;
import com.future.common.util.JsonUtil;
import com.future.common.util.JsonUtilEx;
import com.future.module.system.ModuleDataAuthorizeSchemeApi;
import com.future.module.system.entity.ModuleDataAuthorizeEntity;
import com.future.module.system.entity.ModuleDataAuthorizeSchemeEntity;
import com.future.module.system.model.moduledataauthorizescheme.DataAuthorizeSchemeCrForm;
import com.future.module.system.model.moduledataauthorizescheme.DataAuthorizeSchemeInfoVO;
import com.future.module.system.model.moduledataauthorizescheme.DataAuthorizeSchemeListVO;
import com.future.module.system.model.moduledataauthorizescheme.DataAuthorizeSchemeUpForm;
import com.future.module.system.service.ModuleDataAuthorizeSchemeService;

import javax.validation.Valid;
import java.util.List;

/**
 * 数据权限方案
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2019年9月27日 上午9:18
 */
@Tag(name = "数据权限方案", description = "ModuleDataAuthorizeScheme")
@RestController
@RequestMapping("/ModuleDataAuthorizeScheme")
public class ModuleDataAuthorizeSchemeController extends SuperController<ModuleDataAuthorizeSchemeService, ModuleDataAuthorizeSchemeEntity> implements ModuleDataAuthorizeSchemeApi {

    @Autowired
    private ModuleDataAuthorizeSchemeService schemeService;

    /**
     * 列表
     *
     * @param moduleId 功能主键
     * @return ignore
     */
    @Operation(summary = "方案列表")
    @Parameters({
            @Parameter(name = "moduleId", description = "功能主键", required = true)
    })
    @GetMapping("/{moduleId}/List")
    public ActionResult<ListVO<DataAuthorizeSchemeListVO>> list(@PathVariable("moduleId") String moduleId) {
        List<ModuleDataAuthorizeSchemeEntity> data = schemeService.getList(moduleId);
        List<DataAuthorizeSchemeListVO> list = JsonUtil.getJsonToList(data, DataAuthorizeSchemeListVO.class);
        ListVO<DataAuthorizeSchemeListVO> vo = new ListVO<>();
        vo.setList(list);
        return ActionResult.success(vo);
    }

    /**
     * 信息
     *
     * @param id 主键值
     * @return ignore
     * @throws DataException ignore
     */
    @Operation(summary = "获取方案信息")
    @Parameters({
            @Parameter(name = "id", description = "主键值", required = true)
    })
    @SaCheckPermission("system.menu")
    @GetMapping("/{id}")
    public ActionResult<DataAuthorizeSchemeInfoVO> info(@PathVariable("id") String id) throws DataException {
        ModuleDataAuthorizeSchemeEntity entity = schemeService.getInfo(id);
        DataAuthorizeSchemeInfoVO vo = JsonUtilEx.getJsonToBeanEx(entity, DataAuthorizeSchemeInfoVO.class);
        return ActionResult.success(vo);
    }

    /**
     * 新建
     *
     * @param dataAuthorizeSchemeCrForm 实体对象
     * @return ignore
     */
    @Operation(summary = "新建方案")
    @Parameters({
            @Parameter(name = "dataAuthorizeSchemeCrForm", description = "实体对象", required = true)
    })
    @SaCheckPermission("system.menu")
    @PostMapping
    public ActionResult create(@RequestBody @Valid DataAuthorizeSchemeCrForm dataAuthorizeSchemeCrForm) {
        ModuleDataAuthorizeSchemeEntity entity = JsonUtil.getJsonToBean(dataAuthorizeSchemeCrForm, ModuleDataAuthorizeSchemeEntity.class);
        // 判断fullName是否重复
        if (schemeService.isExistByFullName(entity.getId(), entity.getFullName(), entity.getModuleId())) {
            return ActionResult.fail("已存在相同名称");
        }
        // 判断encode是否重复
        if (schemeService.isExistByEnCode(entity.getId(), entity.getEnCode(), entity.getModuleId())) {
            return ActionResult.fail("已存在相同编码");
        }
        schemeService.create(entity);
        return ActionResult.success(MsgCode.SU001.get());
    }

    /**
     * 更新
     *
     * @param id                        主键值
     * @param dataAuthorizeSchemeUpForm 实体对象
     * @return ignore
     */
    @Operation(summary = "更新方案")
    @Parameters({
            @Parameter(name = "id", description = "主键值", required = true),
            @Parameter(name = "dataAuthorizeSchemeUpForm", description = "实体对象", required = true)
    })
    @SaCheckPermission("system.menu")
    @PutMapping("/{id}")
    public ActionResult update(@PathVariable("id") String id, @RequestBody @Valid DataAuthorizeSchemeUpForm dataAuthorizeSchemeUpForm) {
        ModuleDataAuthorizeSchemeEntity entity = JsonUtil.getJsonToBean(dataAuthorizeSchemeUpForm, ModuleDataAuthorizeSchemeEntity.class);
        // 判断encode是否重复
        if ("future_alldata".equals(entity.getEnCode())) {
            return ActionResult.fail("修改失败，该方案不允许编辑");
        }
        // 判断fullName是否重复
        if (schemeService.isExistByFullName(id, entity.getFullName(), entity.getModuleId())) {
            return ActionResult.fail("已存在相同名称");
        }
        // 判断encode是否重复
        if (schemeService.isExistByEnCode(id, entity.getEnCode(), entity.getModuleId())) {
            return ActionResult.fail("已存在相同编码");
        }
        boolean flag = schemeService.update(id, entity);
        if (!flag) {
            return ActionResult.success(MsgCode.FA002.get());
        }
        return ActionResult.success(MsgCode.SU004.get());
    }

    /**
     * 删除
     *
     * @param id 主键值
     * @return ignore
     */
    @Operation(summary = "删除方案")
    @Parameters({
            @Parameter(name = "id", description = "主键值", required = true)
    })
    @SaCheckPermission("system.menu")
    @DeleteMapping("/{id}")
    public ActionResult delete(@PathVariable("id") String id) {
        ModuleDataAuthorizeSchemeEntity entity = schemeService.getInfo(id);
        if (entity != null) {
            schemeService.delete(entity);
            return ActionResult.success(MsgCode.SU003.get());
        }
        return ActionResult.fail(MsgCode.FA003.get());
    }

    @Override
    @GetMapping("/getList")
    public List<ModuleDataAuthorizeSchemeEntity> getList() {
        return schemeService.getList();
    }

    @Override
    @PostMapping("/getListByModuleId")
    public List<ModuleDataAuthorizeSchemeEntity> getListByModuleId(@RequestBody List<String> ids) {
        return schemeService.getListByModuleId(ids);
    }

    @Override
    @PostMapping("/getListByIds")
    public List<ModuleDataAuthorizeSchemeEntity> getListByIds(@RequestBody List<String> ids) {
        return schemeService.getListByIds(ids);
    }
}
