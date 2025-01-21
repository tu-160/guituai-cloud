package com.future.permission;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import com.future.feign.utils.FeignName;
import com.future.permission.entity.OrganizeAdministratorEntity;
import com.future.permission.fallback.OrganizeAdminTratorApiFallback;
import com.future.permission.model.organizeadministrator.OrganizeAdministratorModel;

import java.util.Collection;
import java.util.List;

/**
 * 获取组织信息Api
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-03-24
 */
@FeignClient(name = FeignName.PERMISSION_SERVER_NAME ,fallback = OrganizeAdminTratorApiFallback.class,path = "/organizeAdminIsTrator")
public interface OrganizeAdminTratorApi {


    /**
     * 获取
     * @param userId
     * @return
     */
    @GetMapping("/getListByUserId")
    List<OrganizeAdministratorEntity> getListByUserId(@RequestParam(value = "userId", required = false) String userId,
                                                      @RequestParam(value = "type", required = false) String type);


    /**
     * 获取
     * @return
     */
    @GetMapping("/getOrganizeList")
    List<String> getOrganizeUserList(@RequestParam(value = "type", required = false) String type);

    /**
     * 获取
     * @return
     */
    @GetMapping("/getOrganizeAdministratorList")
    OrganizeAdministratorModel getOrganizeAdministratorList();

    /**
     * 获取
     * @return
     */
    @PostMapping("/saveOrganizeAdminTrator")
    boolean saveOrganizeAdminTrator(@RequestBody OrganizeAdministratorEntity entity);

    @PostMapping("/getInfoByUserId")
    List<OrganizeAdministratorEntity> getInfoByUserId(@RequestParam(value = "userId", required = false) String userId,
                                                      @RequestParam(value = "tenantId", required = false) String tenantId);
}
