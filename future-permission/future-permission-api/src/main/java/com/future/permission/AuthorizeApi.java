package com.future.permission;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.future.feign.utils.FeignName;
import com.future.module.system.model.base.SystemBaeModel;
import com.future.module.system.model.button.ButtonModel;
import com.future.module.system.model.column.ColumnModel;
import com.future.module.system.model.form.ModuleFormModel;
import com.future.module.system.model.module.ModuleModel;
import com.future.module.system.model.resource.ResourceModel;
import com.future.permission.entity.AuthorizeEntity;
import com.future.permission.fallback.AuthorizeApiFallback;
import com.future.permission.model.authorize.AuthorizeConditionModel;
import com.future.permission.model.authorize.AuthorizeVO;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 获取权限信息Api
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-03-24
 */
@FeignClient(name = FeignName.PERMISSION_SERVER_NAME, fallback = AuthorizeApiFallback.class, path = "/Authority")
public interface AuthorizeApi {

    /**
     * 根据对象Id获取列表
     *
     * @param objectId 对象主键
     * @return
     */
    @GetMapping("/GetListByObjectId/{objectId}")
    List<AuthorizeEntity> getListByObjectId(@PathVariable("objectId") String objectId);

    /**
     * 根据对象Id获取列表
     *
     * @param objectId 对象主键
     * @return
     */
    @GetMapping("/GetListByObjectId/{objectId}/{type}")
    List<AuthorizeEntity> getListByObjectId(@PathVariable("objectId") String objectId,@PathVariable("type") String type);

    /**
     * 将查出来的某个对象删除
     * @param queryWrapper
     * @return
     */
    @DeleteMapping("/remove")
    void remove(QueryWrapper<AuthorizeEntity> queryWrapper);

    /**
     * 获取权限集合
     *
     * @param isCache
     * @param singletonOrg
     * @return
     */
    @GetMapping("/getAuthorize")
    AuthorizeVO getAuthorize(@RequestParam("isCache") boolean isCache, @RequestParam("singletonOrg") boolean singletonOrg);

    @PostMapping("/getConditionSql")
    byte[] getConditionSql(@RequestBody AuthorizeConditionModel conditionModel);

    @PostMapping("/getCondition")
    byte[] getCondition(@RequestBody AuthorizeConditionModel conditionModel);

//    /**
//     * 通过角色集合查询系统信息
//     *
//     * @param roleIds
//     * @return
//     */
//    @PostMapping("/findSystem")
//    List<SystemBaeModel> findSystem(@RequestBody List<String> roleIds);

    @GetMapping("/findButton")
    List<ButtonModel> findButton(@RequestParam("objectId") String objectId);
    @GetMapping("/findColumn")
    List<ColumnModel> findColumn(@RequestParam("objectId") String objectId);
    @GetMapping("/findResource")
    List<ResourceModel> findResource(@RequestParam("objectId") String objectId);
    @GetMapping("/findForms")
    List<ModuleFormModel> findForms(@RequestParam("objectId") String objectId);
    @GetMapping("/findButtonAdmin")
    List<ButtonModel> findButtonAdmin(@RequestParam("mark") Integer mark);
    @GetMapping("/findColumnAdmin")
    List<ColumnModel> findColumnAdmin(@RequestParam("mark") Integer mark);
    @GetMapping("/findResourceAdmin")
    List<ResourceModel> findResourceAdmin(@RequestParam("mark") Integer mark);
    @GetMapping("/findFormsAdmin")
    List<ModuleFormModel> findFormsAdmin(@RequestParam("mark") Integer mark);

    /**
     * 通过Item获取权限列表
     *
     * @param itemType
     * @param itemId
     * @return
     */
    @GetMapping("/getAuthorizeByItem")
    List<AuthorizeEntity> getAuthorizeByItem(@RequestParam(value = "itemType", required = false) String itemType,
                                         @RequestParam(value = "itemId", required = false) String itemId);
    @GetMapping("/getAuthorizeByUser")
    AuthorizeVO getAuthorizeByUser(@RequestParam("singletonOrg") boolean singletonOrg);
}
