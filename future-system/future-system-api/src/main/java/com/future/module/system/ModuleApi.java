package com.future.module.system;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import com.future.feign.utils.FeignName;
import com.future.module.system.entity.ModuleEntity;
import com.future.module.system.fallback.ModuleApiFallback;
import com.future.module.system.model.module.ModuleApiByIdAndMarkModel;
import com.future.module.system.model.module.ModuleApiByIdsModel;
import com.future.module.system.model.module.ModuleApiModel;
import com.future.module.system.model.online.VisualMenuModel;

import java.util.List;
/**
 * 调用系统菜单Api
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-03-24
 */
@FeignClient(name = FeignName.SYSTEM_SERVER_NAME , fallback = ModuleApiFallback.class, path = "/Menu")
public interface ModuleApi {

    /**
     * 列表
     *
     * @return
     * @param model
     */
    @PostMapping("/getList")
    List<ModuleEntity> getList(@RequestBody ModuleApiModel model);

    @GetMapping("/getListById/{id}")
    List<ModuleEntity> getList(@PathVariable("id") String id);

    @GetMapping("/getListByModuleId/{ModuleId}")
    List<ModuleEntity> getModuleList(@PathVariable("ModuleId") String ModuleId);

    @GetMapping("/getInfoById")
    ModuleEntity getModuleByList(@RequestParam("ModuleId") String moduleId);

    @PostMapping("/pubulishToSys")
    Integer pubulish(@RequestBody(required = false) VisualMenuModel visualMenuModel);

    /**
     * 获取主系统菜单
     *
     * @return
     * @param model
     */
    @PostMapping("/getMainModule")
    List<ModuleEntity> getMainModule(@RequestBody ModuleApiModel model);

    /**
     * 通过ids获取系统菜单
     *
     *
     * @param model@return
     */
    @PostMapping("/getModuleByIds")
    List<ModuleEntity> getModuleByIds(@RequestBody ModuleApiByIdsModel model);

    /**
     * 通过ids获取系统菜单
     *
     *
     * @param model@return
     */
    @PostMapping("/getModuleBySystemIds")
    List<ModuleEntity> getModuleBySystemIds(@RequestBody ModuleApiByIdsModel model);

    /**
     * 通过ids获取系统菜单
     *
     * @param enCodeList
     * @return
     */
    @PostMapping("/getListByEnCode")
    List<ModuleEntity> getListByEnCode(@RequestBody List<String> enCodeList);

    @PostMapping("/getModuleByPortal")
    List<ModuleEntity> getModuleByPortal(@RequestBody List<String> portalIds);

    @PostMapping("/findModuleAdmin")
    List<ModuleEntity> findModuleAdmin(@RequestBody ModuleApiByIdAndMarkModel model);
}
