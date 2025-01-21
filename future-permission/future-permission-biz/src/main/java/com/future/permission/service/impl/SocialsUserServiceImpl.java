package com.future.permission.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.future.base.service.SuperServiceImpl;
import com.future.permission.entity.SocialsUserEntity;
import com.future.permission.mapper.SocialsUserMapper;
import com.future.permission.service.SocialsUserService;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 流程设计
 *
 * @author Future Platform Group
 * @version v4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2022/7/14 9:33:16
 */
@Service
public class SocialsUserServiceImpl extends SuperServiceImpl<SocialsUserMapper, SocialsUserEntity> implements SocialsUserService {
    @Override
    public List<SocialsUserEntity> getListByUserId(String userId) {
        QueryWrapper<SocialsUserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SocialsUserEntity::getUserId,userId);
        return this.list(queryWrapper);
    }

    @Override
    public List<SocialsUserEntity> getUserIfnoBySocialIdAndType(String socialId, String socialType) {
        QueryWrapper<SocialsUserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SocialsUserEntity::getSocialId,socialId);
        queryWrapper.lambda().eq(SocialsUserEntity::getSocialType,socialType);
        return this.list(queryWrapper);
    }

    @Override
    public List<SocialsUserEntity> getListByUserIdAndSource(String userId, String socialType) {
        QueryWrapper<SocialsUserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SocialsUserEntity::getUserId,userId);
        queryWrapper.lambda().eq(SocialsUserEntity::getSocialType,socialType);
        return this.list(queryWrapper);
    }

    @Override
    public SocialsUserEntity getInfoBySocialId(String socialId,String socialType){
        QueryWrapper<SocialsUserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SocialsUserEntity::getSocialId,socialId);
        queryWrapper.lambda().eq(SocialsUserEntity::getSocialType,socialType);
        return this.getOne(queryWrapper);
    }
}
