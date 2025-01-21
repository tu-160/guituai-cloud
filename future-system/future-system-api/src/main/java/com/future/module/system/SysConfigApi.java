package com.future.module.system;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.future.common.model.BaseSystemInfo;
import com.future.feign.utils.FeignName;
import com.future.module.system.entity.SysConfigEntity;
import com.future.module.system.fallback.SysConfigApiFallback;

import java.util.List;

/**
 * 调用系统配置Api
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-03-24
 */
@FeignClient(name = FeignName.SYSTEM_SERVER_NAME, fallback = SysConfigApiFallback.class, path = "/SysConfig")
public interface SysConfigApi {

    /**
     * 获取BaseSystemInfo
     *
     * @param tenantId
     * @return
     */
    @GetMapping("/getInfo")
    BaseSystemInfo getSysInfo(@RequestParam("tenantId") String tenantId);

    /**
     * 获取系统配置
     *
     * @return
     */
    @GetMapping("/getSysConfigInfo")
    BaseSystemInfo getSysConfigInfo();

    @GetMapping("/getValueByKey")
    String getValueByKey(@RequestParam("keyStr") String keyStr);

    /**
     * 获取系统配置信息
     *
     * @param type
     * @return
     */
    @GetMapping("/getSysConfigInfoByType")
    List<SysConfigEntity> getSysConfigInfoByType(@RequestParam("type") String type, @RequestParam("tenantId") String tenantId);
}
