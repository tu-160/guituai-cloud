package com.future.permission.mapper;

import org.apache.ibatis.annotations.Param;

import com.future.base.mapper.SuperMapper;
import com.future.permission.entity.UserEntity;

import java.util.List;


/**
 * 用户信息
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2019年9月26日 上午9:18
 */
public interface UserMapper extends SuperMapper<UserEntity> {
    /**
     * 获取用户id
     * @return
     */
    List<String> getListId();

    /**
     * 通过组织id获取用户信息
     *
     * @param orgIdList
     * @param gender
     * @return
     */
    List<String> query(@Param("orgIdList") List<String> orgIdList, @Param("account") String account, @Param("dbSchema") String dbSchema, @Param("enabledMark") Integer enabledMark, @Param("gender") String gender);

    /**
     * 通过组织id获取用户信息
     *
     * @param orgIdList
     * @param gender
     * @return
     */
    Long count(@Param("orgIdList") List<String> orgIdList, @Param("account") String account, @Param("dbSchema") String dbSchema, @Param("enabledMark") Integer enabledMark, @Param("gender") String gender);
}
