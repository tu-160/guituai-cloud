package com.future.module.system;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import com.future.feign.utils.FeignName;
import com.future.module.system.entity.SystemEntity;
import com.future.module.system.fallback.SystemApiFallback;
import com.future.module.system.model.base.SystemApiByIdsModel;
import com.future.module.system.model.base.SystemApiListModel;
import com.future.module.system.model.base.SystemApiModel;

import java.util.List;

/**
 * 调用系统菜单Api
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-03-24
 */
@FeignClient(name = FeignName.SYSTEM_SERVER_NAME, fallback = SystemApiFallback.class, path = "/System")
public interface SystemApi {

//    /**
//     * 获取主系统列表
//     *
//     * @return
//     */
//    @GetMapping("/getMainSystem")
//    SystemEntity getMainSystem();

    /**
     * 获取列表
     *
     * @return
     * @param model
     */
    @PostMapping("/getList")
    List<SystemEntity> getList(@RequestBody SystemApiListModel model);

    /**
     * 通过ids获取系统信息
     *
     *
     * @param model@return
     */
    @PostMapping("/getListByIds")
    List<SystemEntity> getListByIds(@RequestBody SystemApiByIdsModel model);

    /**
     * 通过id获取系统信息
     *
     * @param systemId
     * @return
     */
    @GetMapping("/getInfoById")
    SystemEntity getInfoById(@RequestParam("systemId") String systemId);

    /**
     * 通过id获取系统信息
     *
     * @param enCode
     * @return
     */
    @GetMapping("/getInfoByEnCode")
    SystemEntity getInfoByEnCode(@RequestParam("enCode") String enCode);

//    /**
//     * 获取主系统
//     *
//     * @param systemIds
//     * @return
//     */
//    @PostMapping("/getMainSys")
//    List<SystemEntity> getMainSys(@RequestBody List<String> systemIds);

//    /**
//     * 获取当前用户所有系统权限
//     *
//     * @param userInfo
//     * @return
//     */
//    @PostMapping("/getCurrentUserSystem")
//    List<String> getCurrentUserSystem(@RequestBody UserInfo userInfo);

    @PostMapping("/findSystemAdmin")
    List<SystemEntity> findSystemAdmin(@RequestBody SystemApiModel model);
}
