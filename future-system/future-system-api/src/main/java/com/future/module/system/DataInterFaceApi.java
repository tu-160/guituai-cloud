package com.future.module.system;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import com.future.common.base.ActionResult;
import com.future.feign.utils.FeignName;
import com.future.module.system.entity.DataInterfaceEntity;
import com.future.module.system.fallback.DataInterFaceApiFallback;
import com.future.module.system.model.datainterface.DataInterfaceInvokeModel;
import com.future.module.system.model.datainterface.DataInterfacePage;

import java.util.List;
import java.util.Map;

/**
 * 调用数据接口Api
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-03-24
 */
@FeignClient(name = FeignName.SYSTEM_SERVER_NAME, fallback = DataInterFaceApiFallback.class, path = "/DataInterface")
public interface DataInterFaceApi {

    /**
     * 通过id获取数据接口信息
     *
     * @param id
     * @return
     */
    @GetMapping("/getDataInterfaceInfo")
    DataInterfaceEntity getDataInterfaceInfo(@RequestParam("id") String id, @RequestParam("tenantId") String tenantId);

    /**
     * 通过id获取数据接口信息
     *
     * @param id
     * @return
     */
    @PostMapping("/infoToIdById/{id}")
    ActionResult infoToIdById(@PathVariable("id") String id, @RequestBody Map<String, String> parameterMap);
    /**
     * 通过id获取数据接口信息
     *
     * @param id
     * @return
     */
    @PostMapping("/infoToId/{id}")
    ActionResult infoToId(@PathVariable("id") String id);

    /**
     * 调用数据接口
     *
     * @return
     */
    @PostMapping("/invokeById")
    ActionResult invokeById(@RequestBody DataInterfaceInvokeModel dataInterfaceInvokeModel);

    @PostMapping("/{id}/Actions/List")
    ActionResult infoToIdPageList(@PathVariable("id") String id, @RequestBody DataInterfacePage page);

    @PostMapping("/{id}/Actions/InfoByIds")
    ActionResult<List<Map<String, Object>>> infoByIds(@PathVariable("id") String id, @RequestBody DataInterfacePage page);

    @PostMapping("/getInterfaceList")
    List<DataInterfaceEntity> getInterfaceList(@RequestBody List<String> id);

    @GetMapping("/getEntity")
    DataInterfaceEntity getEntity(@RequestParam("id") String id);
}
