package com.future.permission.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.future.base.service.SuperService;
import com.future.permission.entity.UserRelationEntity;
import com.future.permission.model.permission.PermissionModel;
import com.future.permission.model.userrelation.UserRelationForm;

import java.util.List;

/**
 * 用户关系
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2019年9月26日 上午9:18
 */
public interface UserRelationService extends SuperService<UserRelationEntity> {

    /**
     * 根据用户主键获取列表
     *
     * @param userId 用户主键
     * @return
     */
    List<UserRelationEntity> getListByUserId(String userId);

    List<UserRelationEntity> getListByUserIdAndObjType(String userId, String objectType);
    /**
     * 根据用户主键获取列表
     *
     * @param userId 用户主键
     * @return
     */
    List<UserRelationEntity> getListByUserIdAll(List<String> userId);

    /**
     * 根据对象主键获取列表
     *
     * @param objectId 对象主键
     * @return
     */
    List<UserRelationEntity> getListByObjectId(String objectId);

    List<UserRelationEntity> getListByObjectId(String objectId, String objectType);

    /**
     * 根据对象主键获取列表
     *
     * @param objectId 对象主键
     * @return
     */
    List<UserRelationEntity> getListByObjectIdAll(List<String> objectId);

    /**
     * 根据对象主键删除数据
     *
     * @param objId 对象主键
     * @return
     */
    void deleteAllByObjId(String objId);

    /**
     * 删除用户所有的关联关系
     * @param userId 用户ID
     */
    void deleteAllByUserId(String userId);

    /**
     * 信息
     *
     * @param id 主键值
     * @return
     */
    UserRelationEntity getInfo(String id);

    /**
     * 创建
     *
     * @param objectId 对象主键
     * @param entitys  实体对象
     */
    void save(String objectId, List<UserRelationEntity> entitys);

    /**
     * 创建
     *
     * @param list  实体对象
     */
    void save(List<UserRelationEntity> list);

    /**
     * 删除
     *
     * @param ids 主键值
     */
    void delete(String[] ids);

    /**
     * 添加岗位或角色成员
     */
    void saveObjectId(String objectId, UserRelationForm userRelationForm);

    void roleSaveByUserIds(String roleId, List<String> userIds);

    /**
     * 通过用户id查询用户组织关系
     *
     * @param userIds
     */
    List<UserRelationEntity> getRelationByUserIds(List<String> userIds);

    /**
     * 根据对象主键获取列表
     *
     * @param objectType
     * @return
     */
    List<UserRelationEntity> getListByObjectType(String objectType);

    /**
     * 获取用户组织/岗位/角色集合
     * @param userId
     * @return
     */
    List<UserRelationEntity> getListByObjectType(String userId, String objectType);

    /**
     * 获取用户所有组织关系
     *
     * @param userId 用户id
     * @return 组织关系集合
     */
    List<UserRelationEntity> getAllOrgRelationByUserId(String userId);

    /**
     * 获取个人信息页面用户组织/岗位/角色集合
     *
     * @param objectType 归属类型
     */
    List<PermissionModel> getObjectVoList(String objectType);

    /**
     * 判断岗位/角色与用户是否存在关联关系
     *
     * @param objectType 类型
     * @param objectId 岗位/角色ID
     * @return 存在判断
     */
    Boolean existByObj(String objectType, String objectId);

    /**
     * 获取用户组织关联关系，通过组织ID
     */
    List<UserRelationEntity> getListByRoleId(String roleId);

    /**
     * 根据用户id获取关系
     *
     * @param userId 用户主键
     * @param objectType 类型
     * @return
     */
    List<UserRelationEntity> getListByUserId(String userId, String objectType);

    /**
     * 判断组织下有哪些人
     *
     * @param orgIdList 组织id
     * @return
     */
    List<UserRelationEntity> getListByOrgId(List<String> orgIdList);

}
