package com.future.permission;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.future.feign.utils.FeignName;
import com.future.permission.entity.PermissionGroupEntity;
import com.future.permission.fallback.PermissionGroupApiFallback;

import java.util.List;

@FeignClient(name = FeignName.PERMISSION_SERVER_NAME, fallback = PermissionGroupApiFallback.class, path = "/PermissionGroup")
public interface PermissionGroupApi {

    @GetMapping("/getPermissionGroupByUserId")
    List<PermissionGroupEntity> getPermissionGroupByUserId(@RequestParam("userId") String userId);

    @GetMapping("/getPermissionGroupByUserIdAndTenantId")
    List<PermissionGroupEntity> getPermissionGroupByUserIdAndTenantId(@RequestParam(value = "userId", required = false) String userId,
                                                                      @RequestParam(value = "tenantId", required = false) String tenantId,
                                                                      @RequestParam(value = "systemId", required = false) String systemId);

    @GetMapping("/getOrganizeIdByUserIdAndTenantId")
    String getOrganizeIdByUserIdAndTenantId(@RequestParam(value = "userId", required = false) String userId,
                                                           @RequestParam(value = "tenantId", required = false) String tenantId);

    @GetMapping("/getPermissionGroupByModuleId")
    List<PermissionGroupEntity> getPermissionGroupByModuleId(@RequestParam("moduleId") String moduleId);

    @GetMapping("/getInfoById")
    PermissionGroupEntity getInfoById(@RequestParam("id") String id);

    /**
     * 通过ids获取权限组列表
     *
     * @param ids
     * @return
     */
    @PostMapping("/getListByIds")
    List<PermissionGroupEntity> getListByIds(@RequestBody List<String> ids);
}
