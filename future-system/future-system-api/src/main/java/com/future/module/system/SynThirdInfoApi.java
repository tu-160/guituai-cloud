package com.future.module.system;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import com.future.feign.utils.FeignName;
import com.future.module.system.entity.SynThirdInfoEntity;
import com.future.module.system.fallback.SynThirdInfoApiFallback;
import com.future.permission.model.SynOrganizeDeleteModel;
import com.future.permission.model.SynOrganizeModel;
import com.future.permission.model.SynThirdQyModel;
import com.future.permission.model.SysThirdDeleteModel;

/**
 * 第三方工具的公司-部门-用户同步表模型
 *
 * @@version V4.0.0
 * @@copyright 直方信息科技有限公司
 * @@author Future Platform Group
 * @date 2021/4/25 9:17
 */
@FeignClient(name = FeignName.SYSTEM_SERVER_NAME , fallback = SynThirdInfoApiFallback.class, path = "/SynThirdInfo")
public interface SynThirdInfoApi {
    /**
     * 根据本地对象ID获取第三方ID
     * @param thirdType 1:企业微信 2:钉钉
     * @param dataType  1:公司 2:部门 3：用户
     * @param id        dataType对应的对象ID
     * @return
     */
    @GetMapping("/getInfoBySysObjId/{thirdType}/{dataType}/{id}")
    SynThirdInfoEntity getInfoBySysObjId(@PathVariable("thirdType") String thirdType,@PathVariable("dataType") String dataType,@PathVariable("id") String id, @RequestParam("tenantId") String tenantId);

    /**
     * 同步用户到企业微信
     * @param synThirdQyModel
     * @return
     */
    @PostMapping("/createUserSysToQy")
    void createUserSysToQy(@RequestBody SynThirdQyModel synThirdQyModel);

    /**
     * 同步用户到企业微信
     * @param synThirdQyModel
     * @return
     */
    @PostMapping("/createUserSysToDing")
    void createUserSysToDing(@RequestBody SynThirdQyModel synThirdQyModel);

    /**
     * 修改用户后同步用户到企业微信
     * @param synThirdQyModel
     * @return
     */
    @PostMapping("/updateUserSysToQy")
    void updateUserSysToQy(@RequestBody SynThirdQyModel synThirdQyModel);

    /**
     * 修改用户后同步用户到钉钉
     * @param synThirdQyModel
     * @return
     */
    @PostMapping("/updateUserSysToDing")
    void updateUserSysToDing(@RequestBody SynThirdQyModel synThirdQyModel);

    /**
     * 修改用户后同步用户到企业微信
     * @param sysThirdDeleteModel
     * @return
     */
    @PostMapping("/deleteUserSysToQy")
    void deleteUserSysToQy(@RequestBody SysThirdDeleteModel sysThirdDeleteModel);

    /**
     * 删除用户后同步用户到钉钉
     * @param sysThirdDeleteModel
     * @return
     */
    @PostMapping("/deleteUserSysToDing")
    void deleteUserSysToDing(@RequestBody SysThirdDeleteModel sysThirdDeleteModel);

    /**
     * 新建组织或部门后同步用户到企业微信
     * @param synOrganizeModel
     * @return
     */
    @PostMapping("/createDepartmentSysToQy")
    void createDepartmentSysToQy(@RequestBody SynOrganizeModel synOrganizeModel);

    /**
     * 新建组织或部门后同步用户到钉钉
     * @param synOrganizeModel
     * @return
     */
    @PostMapping("/createDepartmentSysToDing")
    void createDepartmentSysToDing(@RequestBody SynOrganizeModel synOrganizeModel);

    /**
     * 修改组织或部门后同步用户到企业微信
     * @param synOrganizeModel
     * @return
     */
    @PostMapping("/updateDepartmentSysToQy")
    void updateDepartmentSysToQy(@RequestBody SynOrganizeModel synOrganizeModel);

    /**
     * 修改组织或部门后同步用户到钉钉
     * @param synOrganizeModel
     * @return
     */
    @PostMapping("/updateDepartmentSysToDing")
    void updateDepartmentSysToDing(@RequestBody SynOrganizeModel synOrganizeModel);

    /**
     * 删除组织或部门后同步用户到企业微信
     * @param synOrganizeDeleteModel
     * @return
     */
    @PostMapping("/deleteDepartmentSysToQy")
    void deleteDepartmentSysToQy(@RequestBody SynOrganizeDeleteModel synOrganizeDeleteModel);

    /**
     * 删除组织或部门后同步用户到企业微信
     * @param synOrganizeDeleteModel
     * @return
     */
    @PostMapping("/deleteDepartmentSysToDing")
    void deleteDepartmentSysToDing(@RequestBody SynOrganizeDeleteModel synOrganizeDeleteModel);

}
