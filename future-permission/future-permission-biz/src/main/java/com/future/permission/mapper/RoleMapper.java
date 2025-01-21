package com.future.permission.mapper;

import org.apache.ibatis.annotations.Param;

import com.future.base.mapper.SuperMapper;
import com.future.permission.entity.RoleEntity;

import java.util.List;

/**
 * 系统角色
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2019年9月26日 上午9:18
 */
public interface RoleMapper extends SuperMapper<RoleEntity> {

    /**
     * 通过组织id获取用户信息
     *
     * @param orgIdList
     * @return
     */
    List<String> query(@Param("orgIdList") List<String> orgIdList, @Param("keyword") String keyword, @Param("globalMark") Integer globalMark, @Param("enabledMark") Integer enabledMark);

    /**
     * 通过组织id获取用户信息
     *
     * @param
     * @param orgIdList
     * @return
     */
    Long count(@Param("orgIdList") List<String> orgIdList, @Param("keyword") String keyword, @Param("globalMark") Integer globalMark, @Param("enabledMark") Integer enabledMark);

}
