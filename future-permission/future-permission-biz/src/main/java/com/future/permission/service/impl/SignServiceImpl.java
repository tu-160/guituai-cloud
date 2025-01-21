package com.future.permission.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.future.base.service.SuperServiceImpl;
import com.future.common.util.RandomUtil;
import com.future.common.util.UserProvider;
import com.future.permission.entity.SignEntity;
import com.future.permission.mapper.SignMapper;
import com.future.permission.service.SignService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 个人签名
 *
 * @author Future Platform Group
 * @copyright 直方信息科技有限公司
 * @date 2022年9月2日 上午9:18
 */
@Service
public class SignServiceImpl extends SuperServiceImpl<SignMapper, SignEntity> implements SignService {

    @Autowired
    private UserProvider userProvider;


    @Override
    public List<SignEntity> getList() {
        QueryWrapper<SignEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SignEntity::getCreatorUserId, userProvider.get().getUserId())
                .orderByDesc(SignEntity::getCreatorTime);

        return this.list(queryWrapper);
    }


    @Override
    public boolean create(SignEntity entity) {
        QueryWrapper<SignEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SignEntity::getIsDefault, 1)
                .eq(SignEntity::getCreatorUserId, userProvider.get().getUserId());
        SignEntity signEntity = this.getOne(queryWrapper);
        if (entity.getIsDefault() == 0) {
            if (signEntity == null) {
                entity.setIsDefault(1);
            }
        } else {
            if (signEntity != null) {
                signEntity.setIsDefault(0);
                this.updateById(signEntity);
            }
        }
        entity.setId(RandomUtil.uuId());
        entity.setCreatorUserId(userProvider.get().getUserId());
        return this.save(entity);
    }


    @Override
    public boolean delete(String id) {
        return this.removeById(id);
    }


    @Override
    public boolean updateDefault(String id) {
        QueryWrapper<SignEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SignEntity::getIsDefault, 1).eq(SignEntity::getCreatorUserId, userProvider.get().getUserId());
        SignEntity signEntity = this.getOne(queryWrapper);
        if (signEntity != null) {
            signEntity.setIsDefault(0);
            this.updateById(signEntity);
        }
        SignEntity entity = this.getById(id);
        if (entity != null) {
            entity.setIsDefault(1);
            return this.updateById(entity);
        }
        return false;
    }


    @Override
    public SignEntity getDefaultByUserId(String id) {
        QueryWrapper<SignEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SignEntity::getIsDefault, 1).eq(SignEntity::getCreatorUserId, id);
        return this.getOne(queryWrapper);
    }


}
