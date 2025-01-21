package com.future.module.system;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.future.feign.utils.FeignName;
import com.future.module.system.entity.ModuleFormEntity;
import com.future.module.system.fallback.ModuleFormApiFallback;

import java.util.List;

/**
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date ：2022/4/7 14:40
 */
@FeignClient(name = FeignName.SYSTEM_SERVER_NAME , fallback = ModuleFormApiFallback.class, path = "/ModuleForm")
public interface ModuleFormApi {
    @GetMapping("/getList")
    List<ModuleFormEntity> getList();

    @PostMapping("/getListByModuleId")
    List<ModuleFormEntity> getListByModuleId(@RequestBody List<String> ids);

    @PostMapping("/getListByIds")
    List<ModuleFormEntity> getListByIds(@RequestBody List<String> ids);
}
