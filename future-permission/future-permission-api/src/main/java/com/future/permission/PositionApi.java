package com.future.permission;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import com.future.common.base.ActionResult;
import com.future.common.exception.DataException;
import com.future.feign.utils.FeignName;
import com.future.permission.entity.PositionEntity;
import com.future.permission.fallback.PositionApiFallback;
import com.future.permission.model.position.PositionInfoVO;

import java.util.List;
import java.util.Map;

/**
 * 获取岗位信息Api
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-03-24
 */
@FeignClient(name = FeignName.PERMISSION_SERVER_NAME, fallback = PositionApiFallback.class, path = "/Position")
public interface PositionApi {

    /**
     * 通过Id获取岗位信息
     *
     * @param id
     * @return
     */
    @GetMapping("/queryInfoById/{id}")
    PositionEntity queryInfoById(@PathVariable("id") String id);

    /**
     * 通过岗位id获取岗位信息
     *
     * @param posiList
     * @return
     */
    @PostMapping("/getPositionName")
    List<PositionEntity> getPositionName(@RequestBody List<String> posiList, @RequestParam(name = "filterEnabledMark", required = false) Boolean filterEnabledMark);

    /**
     * 通过fullName获取岗位信息
     *
     * @param fullName
     * @return
     */
    @GetMapping("/getByFullName/{fullName}")
    PositionEntity getByFullName(@PathVariable("fullName") String fullName);

    @GetMapping("/getPosMap")
    Map<String, Object> getPosMap(@RequestParam ("type") String type);

    @PostMapping("/getListByOrganizeId")
    List<PositionEntity> getListByOrganizeId(@RequestBody List<String> ableDepIds);
}
