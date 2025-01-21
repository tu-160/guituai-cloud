package com.future.module.system.service;

import com.alibaba.fastjson.JSONObject;
import com.future.common.exception.WxErrorException;
import com.future.common.model.BaseSystemInfo;
import com.future.module.system.model.synthirdinfo.QyWebChatDeptModel;
import com.future.module.system.model.synthirdinfo.QyWebChatUserModel;
import com.future.permission.entity.OrganizeEntity;
import com.future.permission.entity.UserEntity;

/**
 * 本系统的公司、部门、用户与企业微信的同步
 *
 * @@version V4.0.0
 * @@copyright 直方信息科技有限公司
 * @@author Future Platform Group
 * @date 2021/4/27 11:12
 */
public interface SynThirdQyService {

    /**
     * 获取企业微信的配置信息
     * @return
     */
    BaseSystemInfo getQyhConfig();

    //------------------------------------本系统同步公司、部门到企业微信-------------------------------------

    /**
     * 本地同步单个公司或部门到企业微信(供调用)
     * @param isBatch   是否批量(批量不受开关限制)
     * @param deptEntity
     * @param accessToken (单条调用时为空)
     * @return
     * @throws WxErrorException
     */
    JSONObject createDepartmentSysToQy(boolean isBatch, OrganizeEntity deptEntity,String accessToken) throws WxErrorException;

    /**
     * 本地更新单个公司或部门到企业微信(供调用)
     * @param isBatch   是否批量(批量不受开关限制)
     * @param deptEntity
     * @param accessToken (单条调用时为空)
     * @return
     * @throws WxErrorException
     */
    JSONObject updateDepartmentSysToQy(boolean isBatch, OrganizeEntity deptEntity,String accessToken) throws WxErrorException;

    /**
     * 本地删除单个公司或部门，同步到企业微信(供调用)
     * @param isBatch   是否批量(批量不受开关限制)
     * @param id        本系统的公司或部门ID
     * @param accessToken (单条调用时为空)
     * @return
     * @throws WxErrorException
     */
    JSONObject deleteDepartmentSysToQy(boolean isBatch, String id,String accessToken) throws WxErrorException;


    //------------------------------------本系统同步用户到企业微信-------------------------------------

    /**
     * 本地用户创建同步到企业微信的成员(单个)
     * @param isBatch   是否批量(批量不受开关限制)
     * @param userEntity
     * @param accessToken (单条调用时为空)
     * @return
     * @throws WxErrorException
     */
    JSONObject createUserSysToQy(boolean isBatch, UserEntity userEntity,String accessToken) throws WxErrorException;

    /**
     * 本地更新用户信息或部门到企业微信的成员信息(单个)
     * @param isBatch   是否批量(批量不受开关限制)
     * @param userEntity
     * @param accessToken (单条调用时为空)
     * @return
     * @throws WxErrorException
     */
    JSONObject updateUserSysToQy(boolean isBatch, UserEntity userEntity,String accessToken) throws WxErrorException;

    /**
     * 本地删除单个用户，同步到企业微信成员
     * @param isBatch   是否批量(批量不受开关限制)
     * @param id   本系统的公司或部门ID
     * @param accessToken (单条调用时为空)
     * @return
     * @throws WxErrorException
     */
    JSONObject deleteUserSysToQy(boolean isBatch, String id,String accessToken) throws WxErrorException;

    /**
     * 企业微信同步更新公司或部门到本地(供调用)
     * @param isBatch   是否批量(批量不受开关限制)
     * @param deptEntity
     * @param accessToken (单条调用时为空)
     * @return
     */
    JSONObject updateDepartmentQyToSys(boolean isBatch, QyWebChatDeptModel deptEntity, String accessToken);

    /**
     * 企业微信同步公司或部门到本地(供调用)
     * @param isBatch   是否批量(批量不受开关限制)
     * @param deptEntity
     * @param accessToken (单条调用时为空)
     * @return
     */
    JSONObject createDepartmentQyToSys(boolean isBatch, QyWebChatDeptModel deptEntity, String accessToken);

    /**
     * 企业微信往本地同步用户
     * @param isBatch   是否批量(批量不受开关限制)
     * @param qyWebChatUserModel
     * @return
     */
    JSONObject createUserQyToSys(boolean isBatch, QyWebChatUserModel qyWebChatUserModel,String access_token)throws Exception;

    /**
     * 企业微信更新用户信息到本地
     * 将组织、用户的信息写入同步表
     */
    JSONObject updateUserQyToSystem(boolean isBatch, QyWebChatUserModel qyWebChatUserModel,String access_token) throws Exception;


}
