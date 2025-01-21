package com.future.permission.service.impl;

import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.future.base.service.SuperServiceImpl;
import com.future.common.util.DateUtil;
import com.future.common.util.RandomUtil;
import com.future.common.util.UserProvider;
import com.future.permission.entity.UserOldPasswordEntity;
import com.future.permission.mapper.UserOldPasswordMapper;
import com.future.permission.service.UserOldPasswordService;
import com.future.permission.service.UserRelationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户信息
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2019年9月26日 上午9:18
 */
@Service
@DSTransactional
public class UserOldPasswordServiceImpl extends SuperServiceImpl<UserOldPasswordMapper, UserOldPasswordEntity> implements UserOldPasswordService {

    @Autowired
    private UserProvider userProvider;
    @Autowired
    private UserRelationService userRelationService;

    @Override
    public List<UserOldPasswordEntity> getList(String userId) {
        QueryWrapper<UserOldPasswordEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserOldPasswordEntity::getUserId,userId);
        queryWrapper.lambda().orderByDesc(UserOldPasswordEntity::getCreatorTime);
        return this.list(queryWrapper);
    }

    @Override
    public Boolean create(UserOldPasswordEntity entity) {
        entity.setId(RandomUtil.uuId());
        entity.setCreatorTime(DateUtil.getNowDate());
        this.save(entity);
        return true;
    }

}
