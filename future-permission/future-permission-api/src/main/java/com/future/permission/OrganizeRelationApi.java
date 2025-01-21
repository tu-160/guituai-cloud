package com.future.permission;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import com.future.feign.utils.FeignName;
import com.future.permission.entity.OrganizeRelationEntity;
import com.future.permission.entity.PermissionGroupEntity;
import com.future.permission.fallback.OrganizeRelationFallback;
import com.future.permission.model.organize.OrganizeConditionModel;
import com.future.permission.model.organize.OrganizeModel;
import com.future.permission.model.organizerelation.AutoGetMajorOrgIdModel;

import java.util.List;

/**
 * 组织关系
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date ：2022/4/6 14:21
 */
@FeignClient(name = FeignName.PERMISSION_SERVER_NAME, fallback = OrganizeRelationFallback.class, path = "/OrganizeRelation")
public interface OrganizeRelationApi {

    @GetMapping("/existByRoleIdAndOrgId")
    Boolean existByRoleIdAndOrgId(@RequestParam("roleId") String roleId, @RequestParam("orgId") String orgId);

    /**
     * 判断是否有可用权限
     *
     * @param userId
     * @param orgId
     * @param systemId
     * @return
     */
    @GetMapping("/checkBasePermission")
    List<PermissionGroupEntity> checkBasePermission(@RequestParam("userId") String userId, @RequestParam("orgId") String orgId, @RequestParam(value = "systemId", required = false) String systemId);

    @PostMapping("/checkBasePermission")
    String autoGetMajorOrganizeId(@RequestBody AutoGetMajorOrgIdModel autoGetMajorOrgIdModel);

    /**
     * 自动获取岗位id
     * @param userId
     * @param organizeId
     * @param positionId
     * @return
     */
    @GetMapping("/autoGetMajorPositionId")
    String autoGetMajorPositionId(@RequestParam("userId")String userId, @RequestParam("organizeId") String organizeId, @RequestParam("positionId") String positionId);

    @PostMapping("/getRelationListByOrganizeId")
    List<OrganizeRelationEntity> getRelationListByOrganizeId(@RequestBody List<String> ableIds,@RequestParam("type") String type);

    @PostMapping("/getOrgIds")
    List<String> getOrgIds(@RequestBody List<String> departIds);

    @PostMapping("/getOrgIdsList")
    List<OrganizeModel> getOrgIdsList(@RequestBody OrganizeConditionModel organizeConditionModel);
}
