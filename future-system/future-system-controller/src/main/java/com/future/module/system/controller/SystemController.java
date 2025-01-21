package com.future.module.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.util.ObjectUtil;

import com.future.base.controller.SuperController;
import com.future.common.base.ActionResult;
import com.future.common.base.vo.ListVO;
import com.future.common.constant.MsgCode;
import com.future.common.util.JsonUtil;
import com.future.common.util.UserProvider;
import com.future.module.message.NoticeApi;
import com.future.module.system.SystemApi;
import com.future.module.system.entity.SystemEntity;
import com.future.module.system.model.base.*;
import com.future.module.system.service.CommonWordsService;
import com.future.module.system.service.SystemService;
import com.google.common.collect.ImmutableMap;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统控制器
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date ：2022/6/21 15:33
 */
@Tag(name = "系统", description = "system")
@RestController
@RequestMapping("/System")
public class SystemController extends SuperController<SystemService, SystemEntity> implements SystemApi {

    @Autowired
    private SystemService systemService;
    @Autowired
    private NoticeApi noticeApi;
    @Autowired
    private UserProvider userProvider;
    @Autowired
    private CommonWordsService commonWordsService;

    /**
     * 获取系统列表
     *
     * @param page 关键字
     * @return ignore
     */
    @Operation(summary = "获取系统列表")
    @SaCheckPermission("system.menu")
    @GetMapping
    public ActionResult<ListVO<SystemListVO>> list(SystemPageVO page) {
        Boolean enabledMark = false;
        if (ObjectUtil.equal(page.getEnabledMark(), "0")) {
            enabledMark = null;
        }
        if (ObjectUtil.equal(page.getEnabledMark(), "1")) {
            enabledMark = true;
        }
        List<SystemEntity> list = systemService.getList(page.getKeyword(), enabledMark, true, page.getSelector(), true, new ArrayList<>());
        List<SystemListVO> jsonToList = JsonUtil.getJsonToList(list, SystemListVO.class);
        return ActionResult.success(new ListVO<>(jsonToList));
    }

    /**
     * 获取系统详情
     *
     * @param id 主键
     * @return ignore
     */
    @Operation(summary = "获取系统详情")
    @Parameters({
            @Parameter(name = "id", description = "主键", required = true)
    })
    @SaCheckPermission("system.menu")
    @GetMapping("/{id}")
    public ActionResult<SystemVO> info(@PathVariable("id") String id) {
        SystemEntity entity = systemService.getInfo(id);
        if (entity == null) {
            return ActionResult.fail(MsgCode.FA001.get());
        }
        SystemVO jsonToBean = JsonUtil.getJsonToBean(entity, SystemVO.class);
        return ActionResult.success(jsonToBean);
    }

    /**
     * 获取系统详情
     *
     * @param id 主键
     * @return ignore
     */
    @Operation(summary = "获取系统详情")
    @Parameters({
            @Parameter(name = "id", description = "主键", required = true)
    })
    @SaCheckPermission("system.menu")
    @GetMapping("/getPermission/{id}")
    public ActionResult<SystemVO> getPermission(@PathVariable("id") String id) {
        SystemEntity entity = systemService.getInfo(id);
        if (entity == null) {
            return ActionResult.fail(MsgCode.FA001.get());
        }
        SystemVO jsonToBean = JsonUtil.getJsonToBean(entity, SystemVO.class);
        return ActionResult.success(jsonToBean);
    }

    /**
     * 新建系统
     *
     * @param systemCrModel 新建模型
     * @return ignore
     */
    @Operation(summary = "新建系统")
    @Parameters({
            @Parameter(name = "systemCrModel", description = "新建模型", required = true)
    })
    @SaCheckPermission("system.menu")
    @PostMapping
    public ActionResult create(@RequestBody SystemCrModel systemCrModel) {
        SystemEntity entity = JsonUtil.getJsonToBean(systemCrModel, SystemEntity.class);
        if (systemService.isExistFullName(entity.getId(), entity.getFullName())) {
            return ActionResult.fail(MsgCode.EXIST001.get());
        }
        if (systemService.isExistEnCode(entity.getId(), entity.getEnCode())) {
            return ActionResult.fail(MsgCode.EXIST002.get());
        }
        systemService.create(entity);
        return ActionResult.success(MsgCode.SU001.get());
    }

    /**
     * 修改系统
     *
     * @param id            主键
     * @param systemUpModel 修改模型
     * @return ignore
     */
    @Operation(summary = "修改系统")
    @Parameters({
            @Parameter(name = "id", description = "主键", required = true),
            @Parameter(name = "systemCrModel", description = "修改模型", required = true)
    })
    @SaCheckPermission("system.menu")
    @PutMapping("/{id}")
    public ActionResult update(@PathVariable("id") String id, @RequestBody SystemUpModel systemUpModel) {
        SystemEntity systemEntity = systemService.getInfo(id);
        if (systemEntity == null) {
            return ActionResult.fail(MsgCode.FA002.get());
        }
        // 主系统不允许禁用
        if (systemEntity.getIsMain() != null && systemEntity.getIsMain() == 1) {
            if (systemUpModel.getEnabledMark() == 0) {
                return ActionResult.fail("更新失败，主系统不允许禁用");
            }
            if (!systemEntity.getEnCode().equals(systemUpModel.getEnCode())) {
                return ActionResult.fail("更新失败，主系统不允许修改编码");
            }
        }
        SystemEntity entity = JsonUtil.getJsonToBean(systemUpModel, SystemEntity.class);
        entity.setIsMain(systemEntity.getIsMain() != null ? systemEntity.getIsMain() : 0);
        if (systemService.isExistFullName(id, entity.getFullName())) {
            return ActionResult.fail(MsgCode.EXIST001.get());
        }
        if (systemService.isExistEnCode(id, entity.getEnCode())) {
            return ActionResult.fail(MsgCode.EXIST002.get());
        }
        systemService.update(id, entity);
        if (systemEntity.getEnabledMark() == 1 && entity.getEnabledMark() == 0) {
            // 通知下线
            noticeApi.autoSystem(ImmutableMap.of("entity", systemEntity, "message", "应用已被禁用，正为您切换应用"));
        }
        return ActionResult.success(MsgCode.SU004.get());
    }

    /**
     * 删除系统
     *
     * @param id 主键
     * @return ignore
     */
    @Operation(summary = "删除系统")
    @Parameters({
            @Parameter(name = "id", description = "主键", required = true)
    })
    @SaCheckPermission("system.menu")
    @DeleteMapping("/{id}")
    public ActionResult<String> delete(@PathVariable("id") String id) {
        SystemEntity entity = systemService.getInfo(id);
        if (entity == null) {
            return ActionResult.fail(MsgCode.FA003.get());
        }
        if (ObjectUtil.equal(entity.getIsMain(), 1)) {
            return ActionResult.fail("主系统不允许删除");
        }
        // 系统绑定审批常用语时不允许被删除
        if (commonWordsService.existSystem(id)) {
            return ActionResult.fail("系统在审批常用语中被使用，不允许删除");
        } else {
            systemService.delete(id);
            // 通知下线
            noticeApi.autoSystem(ImmutableMap.of("entity", entity, "message", "应用已被删除，正为您切换应用"));
        }
        return ActionResult.success(MsgCode.SU003.get());
    }

//    @Override
//    @GetMapping("/getMainSystem")
//    public SystemEntity getMainSystem() {
//        return systemService.getMainSystem();
//    }

    @Override
    @PostMapping("/getList")
    public List<SystemEntity> getList(@RequestBody SystemApiListModel model) {
        return systemService.getList(model.getKeyword(), model.getFilterEnableMark(), model.getVerifyAuth(), model.getFilterMain(), model.getIsList(), model.getModuleAuthorize());
    }

    @Override
    @PostMapping("/getListByIds")
    public List<SystemEntity> getListByIds(@RequestBody SystemApiByIdsModel model) {
        return systemService.getListByIds(model.getIds(), model.getModuleAuthorize());
    }

    @Override
    @GetMapping("/getInfoById")
    public SystemEntity getInfoById(@RequestParam("systemId") String systemId) {
        return systemService.getInfo(systemId);
    }

    @Override
    @GetMapping("/getInfoByEnCode")
    public SystemEntity getInfoByEnCode(@RequestParam("enCode") String enCode) {
        return systemService.getInfoByEnCode(enCode);
    }

//    @Override
//    @PostMapping("/getMainSys")
//    public List<SystemEntity> getMainSys(@RequestBody List<String> systemIds) {
//        return systemService.getMainSys(systemIds);
//    }

//    @Override
//    @PostMapping("/getCurrentUserSystem")
//    public List<String> getCurrentUserSystem(@RequestBody UserInfo userInfo) {
//        userInfo = UserProvider.getUser();
//        return XSSEscape.escapeObj(systemService.getCurrentUserSystem(userInfo));
//    }

    @Override
    @PostMapping("/findSystemAdmin")
    public List<SystemEntity> findSystemAdmin(@RequestBody SystemApiModel model) {
        return systemService.findSystemAdmin(model.getMark(), model.getMainSystemCode(), model.getModuleAuthorize());
    }

}
