package com.future.module.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.future.common.base.ActionResult;
import com.future.common.base.vo.PageListVO;
import com.future.common.constant.MsgCode;
import com.future.common.util.UserProvider;
import com.future.common.util.XSSEscape;
import com.future.module.system.PortalManageApi;
import com.future.module.system.entity.PortalManageEntity;
import com.future.module.system.model.portalManage.*;
import com.future.module.system.service.PortalManageService;
import com.future.permission.AuthorizeApi;
import com.future.permission.UserApi;
import com.future.permission.entity.AuthorizeEntity;
import com.future.permission.entity.UserEntity;
import com.future.permission.model.portalManage.AuthorizePortalManagePrimary;
import com.future.visualdev.portal.PortalApi;
import com.future.visualdev.portal.constant.PortalConst;

import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 门户管理
 *
 * @author Future Platform Group
 * @version v4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2023-02-16
 */
@Slf4j
@RestController
@Tag(name = "门户管理", description = "PortalManage")
@RequestMapping("/PortalManage")
public class PortalManageController implements PortalManageApi{

    @Autowired
    PortalManageService portalManageService;
    @Autowired
    PortalApi portalApi;
    @Autowired
    private AuthorizeApi authorizeApi;

    @Operation(summary = "新增")
    @PostMapping
    public ActionResult<String> create(@RequestBody @Valid PortalManageCreForm portalManageForm) {
        PortalManageEntity entity = portalManageForm.convertEntity();
        try {
            portalManageService.checkCreUp(entity);
        } catch (Exception e) {
            return ActionResult.fail(e.getMessage());
        }
        portalManageService.save(entity);
        return ActionResult.success(MsgCode.SU018.get());
    }

    @Operation(summary = "删除")
    @DeleteMapping("/{id}")
    public ActionResult<String> delete(@PathVariable String id) {
        boolean flag = portalManageService.removeById(id);
        if(flag){
            // 删除绑定的所有权限
            authorizeApi.remove(new AuthorizePortalManagePrimary(null, id).getQuery());
            return ActionResult.success(MsgCode.SU003.get());
        } else {
            return ActionResult.fail("删除失败");
        }
    }

    @Operation(summary = "编辑")
    @PutMapping("/{id}")
    public ActionResult<String> update(@PathVariable("id") String id, @RequestBody @Valid PortalManageUpForm portalManageUpForm){
        PortalManageEntity update = portalManageUpForm.convertEntity();
        try {
            portalManageService.checkCreUp(update);
        } catch (Exception e) {
            return ActionResult.fail(e.getMessage());
        }
        portalManageService.updateById(update);
        return ActionResult.success(MsgCode.SU004.get());
    }

    @Operation(summary = "查看")
    @GetMapping("/{id}")
    public ActionResult<PortalManageVO> getOne(@PathVariable("id") String id) {
        PortalManageEntity entity = portalManageService.getById(id);
        return ActionResult.success(portalManageService.convertVO(entity));
    }

    @Operation(summary = "列表")
    @GetMapping("/list/{systemId}")
    public ActionResult<PageListVO<PortalManageVO>> getPage(@PathVariable("systemId") String systemId, PortalManagePage pmPage) {
        pmPage.setSystemId(systemId);
        return ActionResult.page(
                portalManageService.getPage(pmPage).getRecords()
                        .stream().map(PortalManagePageDO::convert).collect(Collectors.toList()),
                pmPage.getPaginationVO());
    }

    @Override
    @Operation(summary = "获取集合")
    @GetMapping("/getList")
    public List<PortalManageVO> getList(@RequestParam("systemId") String systemId, @RequestParam("platform") String platform) {
        return portalManageService.getList(new PortalManagePrimary(platform, null, systemId));
    }

    @Override
    @GetMapping("/getListByPortalIdAndPlatform")
    public List<PortalManageVO> getListByPortalIdAndPlatform(@RequestParam("portalId") String portalId, @RequestParam("platform") String platform) {
        return portalManageService.getList(new PortalManagePrimary(platform, portalId, null));
    }

    @Override
    @Operation(summary = "获取有效集合")
    @GetMapping("/getListByEnable")
    public List<PortalManageVO> getListByEnable(@RequestParam("systemId") String systemId, @RequestParam("platform") String platform) {
        PortalManagePrimary primary = new PortalManagePrimary(platform, null, systemId);
        primary.getQuery().lambda().eq(PortalManageEntity::getEnabledMark, 1);
        return portalManageService.getList(primary);
    }

    @Override
    @Operation(summary = "获取所有信息")
    @GetMapping("/getAll")
    public List<PortalManageEntity> getAll() {
        return portalManageService.list();
    }

    @Override
    @PostMapping("/listByIdsAndPlatform")
    public List<PortalManageEntity> listByIdsAndPlatform(@RequestBody List<String> ids, @RequestParam("platform") String platform){
        QueryWrapper<PortalManageEntity> query = new QueryWrapper<>();
        query.lambda().eq(PortalManageEntity::getEnabledMark, 1)
                .eq(PortalManageEntity::getPlatform, platform)
                .in(PortalManageEntity::getId, ids);
        return portalManageService.list(query);
    }

    @Override
    @Operation(summary = "获取集合根据门户Id")
    @GetMapping("/listByPortalId")
    public List<PortalManageVO> getListByPortalId(@RequestParam("portalId")String portalId) {
        return portalManageService.getList(new PortalManagePrimary(null, portalId, null));
    }

    @Override
    @PostMapping("/createBatch")
    @GlobalTransactional
    public void createBatch(@RequestParam("platform")String platform, @RequestParam("portalId")String portalId,
                            @RequestBody List<String> systemIdList) {
        try {
            portalManageService.createBatch(systemIdList.stream().map(systemId->
                    new PortalManagePrimary(platform, portalId, systemId)).collect(Collectors.toList()));
        }catch (Exception e){

        }
    }

    @Override
    @PostMapping("/getSelectList")
    public List<PortalManagePageDO> getSelectList(@RequestBody PortalManagePage portalManagePage) {
        return XSSEscape.escapeObj(portalManageService.getSelectList(XSSEscape.escapeObj(portalManagePage)));
    }

    @Override
    @PostMapping("/selectPortalBySystemIds")
    public List<PortalManagePageDO> selectPortalBySystemIds(@RequestBody PortalManageSelectModel model) {
        return portalManageService.selectPortalBySystemIds(model.getSystemIds(), model.getCollect());
    }

}
