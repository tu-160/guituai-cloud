package com.future.permission;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import com.future.feign.utils.FeignName;
import com.future.permission.entity.OrganizeEntity;
import com.future.permission.fallback.OrganizeApiFallback;
import com.future.permission.model.organize.OrganizeConditionModel;

import java.util.List;
import java.util.Map;

/**
 * 获取组织信息Api
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-03-24
 */
@FeignClient(name = FeignName.PERMISSION_SERVER_NAME, fallback = OrganizeApiFallback.class, path = "/Organize")
public interface SignApi {
    /**
     * 通过id获取组织信息
     *
     * @param organizeId
     * @return
     */
    @GetMapping("/getInfoById/{organizeId}")
    OrganizeEntity getInfoById(@PathVariable("organizeId") String organizeId);

}
