package com.future.permission;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import com.future.feign.utils.FeignName;
import com.future.permission.entity.UserRelationEntity;
import com.future.permission.fallback.UserRelationApiFallback;
import com.future.permission.model.authorize.AuthorizeListModel;
import com.future.permission.model.permission.PermissionModel;

import java.util.List;

/**
 * 获取用户关系Api
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-03-24
 */
@FeignClient(name = FeignName.PERMISSION_SERVER_NAME , fallback = UserRelationApiFallback.class, path = "/UserRelation")
public interface UserRelationApi {

    /**
     * 获取用户关系列表
     *
     * @return
     */
    @GetMapping("/getList/{userId}")
    List<UserRelationEntity> getList(@PathVariable("userId") String userId);

    /**
     * 获取用户关系列表
     *
     * @return
     */
    @GetMapping("/getList")
    List<UserRelationEntity> getList(@RequestParam("userId") String userId, @RequestParam("objectType") String objectType);

    /**
     * 获取用户关系列表
     *
     * @return
     */
    @PostMapping("/getListByUserIdAll")
    List<UserRelationEntity> getListByUserIdAll(@RequestBody List<String> id);

    /**
     * 获取用户关系列表
     *
     * @return
     */
    @PostMapping("/getListByObjectIdAll")
    List<UserRelationEntity> getListByObjectIdAll(@RequestBody List<String> id);

    /**
     * 获取个人信息页面用户组织/岗位/角色集合
     *
     */
    @GetMapping("/getObjectVoList/{objectType}")
    List<PermissionModel> getObjectVoList(@PathVariable("objectType")String objectType);
}
