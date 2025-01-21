package com.future.module.system;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import com.future.feign.utils.FeignName;
import com.future.module.system.entity.ProvinceEntity;
import com.future.module.system.fallback.AreaApiFallback;

import java.util.List;

/**
 * 获取行政区划Api
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-03-24
 */
@FeignClient(name = FeignName.SYSTEM_SERVER_NAME , fallback = AreaApiFallback.class, path = "/Area")
public interface AreaApi {
    /**
     * 获取行政区划列表
     * @param id
     * @return
     */
    @GetMapping("/getList/{id}")
    List<ProvinceEntity> getList(@PathVariable("id") String id);

    /**
     * 获取行政区划列表
     * @param ids
     * @return
     */
    @PostMapping("/getByIdList")
    List<ProvinceEntity> getByIdList(@RequestBody List<String> ids);

    @GetMapping("/getAllProList")
    List<ProvinceEntity> getAllProList();

    @GetMapping("/getProListBytype/{type}")
    List<ProvinceEntity> getProListBytype(@PathVariable("type") String type);

    @PostMapping("/getProvinceByParent")
    ProvinceEntity getProListBytype(@RequestParam("id") String id,@RequestBody List<String> parentIds);
}
