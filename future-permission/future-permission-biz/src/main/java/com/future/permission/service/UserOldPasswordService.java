package com.future.permission.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.future.base.service.SuperService;
import com.future.common.base.Page;
import com.future.common.base.Pagination;
import com.future.common.base.vo.DownloadVO;
import com.future.permission.entity.UserEntity;
import com.future.permission.entity.UserOldPasswordEntity;
import com.future.permission.model.user.PaginationUser;
import com.future.permission.model.user.UserByRoleVO;
import com.future.permission.model.user.UserExportVO;
import com.future.permission.model.user.UserImportVO;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 用户信息
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2019年9月26日 上午9:18
 */
public interface UserOldPasswordService extends SuperService<UserOldPasswordEntity> {

    /**
     * 列表
     *
     * @return
     */
    List<UserOldPasswordEntity>  getList(String userId);

    /**
     * 创建
     *
     * @param entity 实体对象
     */
    Boolean create(UserOldPasswordEntity entity);

}
