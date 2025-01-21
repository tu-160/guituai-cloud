package com.future.permission;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import com.future.feign.utils.FeignName;
import com.future.permission.entity.RoleEntity;
import com.future.permission.fallback.RoleApiFallback;
import com.future.permission.model.role.RoleInfoModel;

import java.util.List;
import java.util.Map;

/**
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date ：2022/4/6 14:19
 */
@FeignClient(name = FeignName.PERMISSION_SERVER_NAME, fallback = RoleApiFallback.class, path = "/Role")
public interface RoleApi {

    /**
     * 通过id获取角色信息(登录前)
     *
     * @param tenantId
     * @param dbName
     * @param id
     * @return
     */
    @PostMapping("/getListByUserId")
    List<RoleEntity> getListByUserId(@RequestBody RoleInfoModel roleInfoModel);

    @PostMapping("/getByUserId")
    List<RoleEntity> getByUserId(@RequestParam("userId") String userId);

    /**
     * 通过id获取角色信息
     *
     * @param id
     * @return
     */
    @GetMapping("/getInfoById/{id}")
    RoleEntity getInfoById(@PathVariable("id") String id);

    /**
     * 当前用户拥有的所有角色集合
     *
     * @return
     */
    @GetMapping("/getRoleIdsByCurrentUser")
    List<String> getRoleIdsByCurrentUser();

    /**
     * 通过角色id集合获取角色信息
     *
     * @param roleIds
     * @return
     */
    @PostMapping("/getListByIds")
    List<RoleEntity> getListByIds(@RequestBody List<String> roleIds);

    @GetMapping("/getAllRoleIdsByUserIdAndOrgId")
    List<String> getAllRoleIdsByUserIdAndOrgId(@RequestParam("userId")String userId, @RequestParam("orgId")String orgId);

    @GetMapping("/getListAll")
    List<RoleEntity> getListAll();

    @GetMapping("/getRoleMap")
    Map<String, Object> getRoleMap(@RequestParam ("type")String type);
}
