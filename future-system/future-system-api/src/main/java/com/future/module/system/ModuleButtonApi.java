package com.future.module.system;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.future.feign.utils.FeignName;
import com.future.module.system.entity.ModuleButtonEntity;
import com.future.module.system.fallback.ModuleButtonApiFallback;

import java.util.List;

/**
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date ：2022/4/7 14:37
 */
@FeignClient(name = FeignName.SYSTEM_SERVER_NAME , fallback = ModuleButtonApiFallback.class, path = "/ModuleButton")
public interface ModuleButtonApi {

    @GetMapping("/getList")
    List<ModuleButtonEntity> getList();

    /**
     * 通过moduleIds获取按钮权限
     *
     * @param ids
     * @return
     */
    @PostMapping("/getListByModuleId")
    List<ModuleButtonEntity> getListByModuleIds(@RequestBody List<String> ids);

    /**
     * 通过moduleIds获取按钮权限
     *
     * @param ids
     * @return
     */
    @PostMapping("/getListByIds")
    List<ModuleButtonEntity> getListByIds(@RequestBody List<String> ids);

}
