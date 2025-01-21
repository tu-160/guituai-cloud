package com.future.permission;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import com.future.common.base.ActionResult;
import com.future.common.exception.DataException;
import com.future.common.util.NoDataSourceBind;
import com.future.feign.utils.FeignName;
import com.future.permission.entity.UserEntity;
import com.future.permission.fallback.UserApiFallback;
import com.future.permission.model.user.*;

import java.util.List;
import java.util.Map;

/**
 * 获取用户信息Api
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-03-24
 */
@FeignClient(name = FeignName.PERMISSION_SERVER_NAME, fallback = UserApiFallback.class, path = "/Users")
public interface UserApi {

    /**
     * 根据id获取用户
     *
     * @param id 用户id
     * @return
     */
    @PostMapping("/getUserName")
    List<UserEntity> getUserName(@RequestBody List<String> id);


    /**
     * 根据id获取用户
     *
     * @param userModel
     * @return
     */
    @PostMapping("/getUserNamePagination")
    List<UserEntity> getUserNamePagination(@RequestBody UserModel userModel);

    /**
     * 获取用户信息
     *
     * @param id 主键值
     * @return
     */
    @GetMapping("/{id}")
    ActionResult<UserInfoVO> getInfo(@PathVariable("id") String id) throws DataException;

    /**
     * 获取用户id
     *
     * @return
     */
    @GetMapping("/getListId")
    List<String> getListId();

    /**
     * 信息(登录前)
     *
     *
     * @param userInfoModel
     * @return
     */
    @PostMapping("/getInfoByAccount")
    UserEntity getInfoByAccount(@RequestBody UserInfoModel userInfoModel);

    /**
     * 信息(登录前)
     *
     *
     * @param userInfoModel@return
     */
    @PostMapping("/getInfoByUserId")
    UserEntity getInfoByUserId(@RequestBody UserInfoModel userInfoModel);

    /**
     * 通过id修改(登录前)
     *
     * @param userUpdateModel
     */
    @PostMapping("/updateById")
    Boolean updateById(@RequestBody UserUpdateModel userUpdateModel);

    @PostMapping("/update/{id}")
    Boolean update(@PathVariable("id") String id, @RequestBody UserEntity entity) throws Exception;

    /**
     * 通过account获取用户信息
     *
     * @param account
     * @return
     */
    @GetMapping("/getInfoByAccount/{account}")
    UserEntity getInfoByAccount(@PathVariable("account") String account);

    @GetMapping("/getAccountIsExist/{account}")
    Boolean getAccountIsExist(@PathVariable("account") String account);


    /**
     * 通过Id获取用户信息
     *
     * @param id 主键值
     * @return
     */
    @GetMapping("/getInfoById/{id}")
    UserEntity getInfoById(@PathVariable("id") String id);


    /**
     * 通过Id获取用户信息
     *
     * @param id 主键值
     * @return
     */
    @NoDataSourceBind
    @GetMapping("/getInfoByIdInMessage")
    UserEntity getInfoByIdInMessage(@RequestParam("id") String id);

    /**
     * 通过Id获取主管信息
     *
     * @param id 主键值
     * @return
     */
    @GetMapping("/getListByManagerId/{id}")
    List<UserEntity> getListByManagerId(@PathVariable("id") String id);

    /**
     * 获取用户信息
     *
     * @return
     * @param enabledMark
     */
    @GetMapping("/getList")
    List<UserEntity> getList(@RequestParam("enabledMark") boolean enabledMark);

    /**
     * 获取超级管理员
     *
     * @return
     */
    @GetMapping("/getAdminList")
    List<UserEntity> getAdminList();

    /**
     * 修改超级管理员
     *
     * @param adminIds
     * @return
     */
    @PostMapping("/setAdminListByIds")
    Boolean setAdminListByIds(@RequestBody List<String> adminIds);


    /**
     * 通过fullName获取用户信息
     *
     * @param fullName
     * @return
     */
    @GetMapping("/getByRealName/{fullName}")
    UserEntity getByRealName(@PathVariable("fullName") String fullName);

    @PostMapping("/updateUserById")
    Boolean updateUserById(@RequestBody UserEntity userEntity);

    @GetMapping("/getUserMap")
    Map<String, Object> getUserMap(@RequestParam("type") String type);

    /**
     * 通过mobile获取用户信息
     *
     * @param mobile
     * @return
     */
    @GetMapping("/getInfoByMobile/{mobile}")
    UserEntity getInfoByMobile(@PathVariable("mobile") String mobile);

    @PostMapping("/create")
    Boolean create(@RequestBody UserEntity userEntity) throws Exception;

    @PostMapping("/delete")
    void delete(@RequestBody UserEntity userEntity);


    @PostMapping("/getUserByRoleList/{organizeId}")
    List<UserByRoleVO> getUserByRoleList(@PathVariable("organizeId") String organizeId);

    @PostMapping("/getUserIdList")
    List<String> getUserIdList(@RequestBody List<String> userIds);

    @PostMapping("/getDefaultCurrentUserId")
    String getDefaultCurrentUserId(@RequestBody UserConditionModel userConditionModel);

    @PostMapping("/selectedByIds")
    List<UserIdListVo> selectedByIds(@RequestBody List<String> ids);

    @PostMapping("/getUserNameMark")
    List<UserEntity> getUserNameMark(@RequestBody List<String> id,@RequestParam(name = "getUserNameMark", required = false) boolean filterEnabledMark);
}
