package com.future.permission.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.future.base.controller.SuperController;
import com.future.permission.OrganizeRelationApi;
import com.future.permission.entity.OrganizeRelationEntity;
import com.future.permission.entity.PermissionGroupEntity;
import com.future.permission.model.organize.OrganizeConditionModel;
import com.future.permission.model.organize.OrganizeModel;
import com.future.permission.model.organizerelation.AutoGetMajorOrgIdModel;
import com.future.permission.service.OrganizeRelationService;

import java.util.List;

/**
 * 组织关系控制器
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date ：2022/4/7 10:27
 */
@RestController
@RequestMapping("/OrganizeRelation")
public class OrganizeRelationController extends SuperController<OrganizeRelationService, OrganizeRelationEntity> implements OrganizeRelationApi {

    @Autowired
    private OrganizeRelationService organizeRelationService;

    @Override
    @GetMapping("/existByRoleIdAndOrgId")
    public Boolean existByRoleIdAndOrgId(@RequestParam("roleId") String roleId, @RequestParam("orgId") String orgId) {
        return organizeRelationService.existByRoleIdAndOrgId(roleId, orgId);
    }

    @Override
    @GetMapping("/checkBasePermission")
    public List<PermissionGroupEntity> checkBasePermission(@RequestParam("userId") String userId, @RequestParam("orgId") String orgId, @RequestParam(value = "systemId", required = false) String systemId) {
        return organizeRelationService.checkBasePermission(userId, orgId, systemId);
    }

    @Override
    @PostMapping("/checkBasePermission")
    public String autoGetMajorOrganizeId(@RequestBody AutoGetMajorOrgIdModel autoGetMajorOrgIdModel) {
        return organizeRelationService.autoGetMajorOrganizeId(autoGetMajorOrgIdModel.getUserId(), autoGetMajorOrgIdModel.getOrgIds(), autoGetMajorOrgIdModel.getOrganizeId(), autoGetMajorOrgIdModel.getSystemId());
    }

    @Override
    @GetMapping("/autoGetMajorPositionId")
    public String autoGetMajorPositionId(@RequestParam("userId")String userId, @RequestParam("organizeId") String organizeId, @RequestParam( value = "positionId", required = false) String positionId) {
        return organizeRelationService.autoGetMajorPositionId(userId, organizeId, positionId);
    }

    @Override
    @PostMapping("/getRelationListByOrganizeId")
    public List<OrganizeRelationEntity> getRelationListByOrganizeId(@RequestBody List<String> ableIds,@RequestParam("type") String type) {
        return organizeRelationService.getRelationListByOrganizeId( ableIds, type);
    }

    @Override
    @PostMapping("/getOrgIds")
    public List<String> getOrgIds(@RequestBody List<String> departIds) {
        return organizeRelationService.getOrgIds(departIds,null);
    }

    @Override
    @PostMapping("/getOrgIdsList")
    public List<OrganizeModel> getOrgIdsList(@RequestBody OrganizeConditionModel organizeConditionModel) {
        return organizeRelationService.getOrgIdsList(organizeConditionModel);
    }
}
