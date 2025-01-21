package com.future.module.system;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.future.feign.utils.FeignName;
import com.future.module.system.entity.ModuleColumnEntity;
import com.future.module.system.fallback.ModuleColumnApiFallback;

import java.util.List;

/**
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date ：2022/4/7 14:39
 */
@FeignClient(name = FeignName.SYSTEM_SERVER_NAME , fallback = ModuleColumnApiFallback.class, path = "/ModuleColumn")
public interface ModuleColumnApi {
    @GetMapping("/getList")
    List<ModuleColumnEntity> getList();

    /**
     * 通过moduleIds获取权限
     *
     * @param ids
     * @return
     */
    @PostMapping("/getListByModuleId")
    List<ModuleColumnEntity> getListByModuleId(@RequestBody List<String> ids);

    /**
     * 通过moduleIds获取权限
     *
     * @param ids
     * @return
     */
    @PostMapping("/getListByIds")
    List<ModuleColumnEntity> getListByIds(@RequestBody List<String> ids);
}
