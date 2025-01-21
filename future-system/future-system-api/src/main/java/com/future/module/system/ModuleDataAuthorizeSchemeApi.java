package com.future.module.system;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.future.feign.utils.FeignName;
import com.future.module.system.entity.ModuleDataAuthorizeSchemeEntity;
import com.future.module.system.fallback.ModuleDataAuthorizeSchemeApiFallback;

import java.util.List;

/**
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date ：2022/4/7 14:43
 */
@FeignClient(name = FeignName.SYSTEM_SERVER_NAME , fallback = ModuleDataAuthorizeSchemeApiFallback.class, path = "/ModuleDataAuthorizeScheme")
public interface ModuleDataAuthorizeSchemeApi {
    @GetMapping("/getList")
    List<ModuleDataAuthorizeSchemeEntity> getList();

    @PostMapping("/getListByModuleId")
    List<ModuleDataAuthorizeSchemeEntity> getListByModuleId(@RequestBody List<String> ids);

    @PostMapping("/getListByIds")
    List<ModuleDataAuthorizeSchemeEntity> getListByIds(@RequestBody List<String> ids);
}
