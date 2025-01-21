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
public interface OrganizeApi {
    /**
     * 通过id获取组织信息
     *
     * @param organizeId
     * @return
     */
    @GetMapping("/getInfoById/{organizeId}")
    OrganizeEntity getInfoById(@PathVariable("organizeId") String organizeId);

    /**
     * 获取组织信息
     *
     * @return
     */
    @GetMapping("/getList")
    List<OrganizeEntity> getList();

    /**
     * 获取组织信息
     *
     * @return
     */
    @PostMapping("/getOrganizeName")
    List<OrganizeEntity> getOrganizeName(@RequestBody List<String> id);

    /**
     * 通过fullName获取组织信息
     *
     * @param fullName
     * @return
     */
    @GetMapping("/getByFullName/{fullName}")
    OrganizeEntity getByFullName(@PathVariable("fullName") String fullName);


    /**
     * 获取组织信息
     *
     * @return
     */
    @GetMapping("/getOrganizeId/{organizeId}")
    List<OrganizeEntity> getOrganizeId(@PathVariable("organizeId") String organizeId);


    /**
     * 通过组织id树获取名称
     *
     * @param organizeIdTree 组织id树
     * @return 组织对象集合
     */
    @GetMapping("/organizeIdTree")
    String getFullNameByOrgIdTree(@RequestParam("organizeIdTree") String organizeIdTree);

    @GetMapping("/getOrgMap")
    Map<String,Object> getOrgMap(@RequestParam("type") String type,@RequestParam(value = "category",required = false) String category);

    /**
     * 获取父级组织id
     *
     * @param organizeEntity
     * @return
     */
    @PostMapping("/getOrganizeIdTree")
    String getOrganizeIdTree(@RequestBody OrganizeEntity organizeEntity);

    @PostMapping("/save")
    void save(@RequestBody OrganizeEntity organizeEntity);

    @GetMapping("/getOrganizeByParentId")
    List<OrganizeEntity> getOrganizeByParentId();

    @PutMapping("/updateOrganizeEntity/{organizeId}")
    void updateOrganizeEntity(@PathVariable("organizeId") String organizeId,@RequestBody OrganizeEntity organizeEntity);

    @GetMapping("/getOrganizeDepartmentAll/{organize}")
    List<OrganizeEntity> getOrganizeDepartmentAll(@PathVariable("organize") String organize);

    /**
     * 获取所有当前用户的组织及子组织
     *
     * @param organizeId
     * @return
     */
    @GetMapping("/getUnderOrganizations/{organizeId}")
    List<String> getUnderOrganizations(@PathVariable("organizeId")String organizeId);

    @GetMapping("/upWardRecursion")
    List<String> upWardRecursion(@RequestParam("organizeId")String organizeId);

    @PostMapping("/getDefaultCurrentDepartmentId")
    String getDefaultCurrentDepartmentId(@RequestBody OrganizeConditionModel organizeConditionModel);

    @PostMapping("/getOrganizeChildList")
    List<OrganizeEntity> getOrganizeChildList(@RequestBody List<String> id);

    @GetMapping("/getOrgMapsAll")
    Map<String, OrganizeEntity> getOrgMapsAll();

    @GetMapping("/removeOrganizeInfoList")
    void removeOrganizeInfoList();

    @GetMapping("/getInfoList")
    Map<String, String> getInfoList();

    @GetMapping("/getAllOrgsTreeName")
    Map<String, Object> getAllOrgsTreeName();
}
