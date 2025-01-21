package com.future.permission.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import cn.hutool.core.util.ArrayUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.future.base.controller.SuperController;
import com.future.common.annotation.UserPermission;
import com.future.common.base.ActionResult;
import com.future.common.constant.MsgCode;
import com.future.common.constant.PermissionConst;
import com.future.permission.UserRelationApi;
import com.future.permission.entity.UserEntity;
import com.future.permission.entity.UserRelationEntity;
import com.future.permission.model.permission.PermissionModel;
import com.future.permission.model.userrelation.UserRelationForm;
import com.future.permission.model.userrelation.UserRelationIdsVO;
import com.future.permission.service.UserRelationService;
import com.future.permission.service.UserService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户关系
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2019年9月26日 上午9:18
 */
@Tag(name = "用户关系", description = "UserRelation")
@RestController
@RequestMapping("/UserRelation")
public class UserRelationController extends SuperController<UserRelationService, UserRelationEntity> implements UserRelationApi {

    @Autowired
    private UserRelationService userRelationService;
    @Autowired
    private UserService userService;

    /**
     * 列表
     *
     * @param objectId 对象主键
     * @return
     */
    @Operation(summary = "获取岗位/角色/门户成员列表ids")
    @Parameters({
            @Parameter(name = "objectId", description = "对象主键", required = true)
    })
    @SaCheckPermission(value = {"permission.authorize", "permission.position", "permission.role"}, mode = SaMode.OR)
    @GetMapping("/{objectId}")
    public ActionResult<UserRelationIdsVO> listTree(@PathVariable("objectId") String objectId) {
        List<UserRelationEntity> data = userRelationService.getListByObjectId(objectId);
        List<String> ids = new ArrayList<>();
        for (UserRelationEntity entity : data) {
            ids.add(entity.getUserId());
        }
        UserRelationIdsVO vo = new UserRelationIdsVO();
        vo.setIds(ids);
        return ActionResult.success(vo);
    }

    /**
     * 保存
     *
     * @param objectId 对象主键
     * @param userRelationForm 页面数据
     * @return
     */
    @UserPermission
    @Operation(summary = "添加岗位或角色成员")
    @Parameters({
            @Parameter(name = "objectId", description = "对象主键", required = true),
            @Parameter(name = "userRelationForm", description = "页面数据", required = true)
    })
    @SaCheckPermission(value = {"permission.authorize", "permission.position", "permission.role"}, mode = SaMode.OR)
    @PostMapping("/{objectId}")
    public ActionResult save(@PathVariable("objectId") String objectId, @RequestBody UserRelationForm userRelationForm) {
        List<String> userIds = new ArrayList<>();
        if(userRelationForm.getObjectType().equals(PermissionConst.ROLE)){
            // 得到禁用的id
            List<UserRelationEntity> listByObjectId = userRelationService.getListByObjectId(objectId, PermissionConst.ROLE);
            List<String> collect = listByObjectId.stream().map(UserRelationEntity::getUserId).collect(Collectors.toList());
            List<String> collect1 = collect.stream().filter(t -> !userRelationForm.getUserIds().contains(t)).collect(Collectors.toList());
            userIds.addAll(collect1);
            Set<String> set = new HashSet<>(userRelationForm.getUserIds());
            set.addAll(userService.getUserList(collect).stream().map(UserEntity::getId).collect(Collectors.toList()));
            List<String> list = new ArrayList<>(set);
            userRelationService.roleSaveByUserIds(objectId, list);
        } else {
            // 得到禁用的id
            List<UserRelationEntity> listByObjectId = userRelationService.getListByObjectId(objectId, PermissionConst.POSITION);
            List<String> collect = listByObjectId.stream().map(UserRelationEntity::getUserId).collect(Collectors.toList());
            List<String> collect1 = collect.stream().filter(t -> !userRelationForm.getUserIds().contains(t)).collect(Collectors.toList());
            userIds.addAll(collect1);
            Set<String> set = new HashSet<>(userRelationForm.getUserIds());
            set.addAll(userService.getUserList(collect).stream().map(UserEntity::getId).collect(Collectors.toList()));
            List<String> list = new ArrayList<>(set);
            userRelationForm.setUserIds(list);
            userRelationService.saveObjectId(objectId,userRelationForm);
        }

        userService.delCurUser(null, ArrayUtil.toArray(userIds, String.class));
        return ActionResult.success(MsgCode.SU002.get());
    }

    @Override
    @GetMapping("/getList/{userId}")
    public List<UserRelationEntity> getList(@PathVariable("userId") String userId) {
        return userRelationService.getListByUserId(userId);
    }

    @Override
    @GetMapping("/getList")
    public List<UserRelationEntity> getList(@RequestParam("userId") String userId, @RequestParam("objectType") String objectType) {
        return userRelationService.getListByUserId(userId, objectType);
    }

    @Override
    @PostMapping("/getListByUserIdAll")
    public List<UserRelationEntity> getListByUserIdAll(@RequestBody List<String> id) {
        return userRelationService.getListByUserIdAll(id);
    }

    @Override
    @PostMapping("/getListByObjectIdAll")
    public List<UserRelationEntity> getListByObjectIdAll(@RequestBody List<String> id) {
        return userRelationService.getListByObjectIdAll(id);
    }

    @Override
    @GetMapping("/getObjectVoList/{objectType}")
    public List<PermissionModel> getObjectVoList(@PathVariable("objectType")String objectType){
        return userRelationService.getObjectVoList(objectType);
    }

}
