package com.future.permission.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.future.common.util.JsonUtil;
import com.future.common.util.Md5Util;
import com.future.common.util.RandomUtil;
import com.future.common.util.StringUtil;
import com.future.permission.connector.UserInfoService;
import com.future.permission.entity.UserEntity;
import com.future.permission.service.UserService;

/**
 * 用户信息保存
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date ：2022/7/28 14:38
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserService userService;

    @Override
    public Boolean create(Map<String, Object> map) {
        UserEntity entity = JsonUtil.getJsonToBean(map, UserEntity.class);
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserEntity::getAccount, entity.getAccount());
        UserEntity entity1 = userService.getOne(queryWrapper);
        if (entity1 != null) {
            entity.setId(entity1.getId());
            return userService.updateById(entity);
        } else {
            if (StringUtil.isEmpty(entity.getId())) {
                String userId = RandomUtil.uuId();
                entity.setId(userId);
            }
            entity.setSecretkey(RandomUtil.uuId());
            entity.setPassword(Md5Util.getStringMd5(entity.getPassword().toLowerCase() + entity.getSecretkey().toLowerCase()));
            entity.setIsAdministrator(0);
            return userService.save(entity);
        }
    }

    @Override
    public Boolean update(Map<String, Object> map) {
        return create(map);
    }

    @Override
    public Boolean delete(Map<String, Object> map) {
        UserEntity entity = JsonUtil.getJsonToBean(map, UserEntity.class);
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserEntity::getAccount, entity.getAccount());
        UserEntity entity1 = userService.getOne(queryWrapper);
        if (entity1 != null) {
            entity.setId(entity1.getId());
        }
        return userService.removeById(entity.getId());
    }

    @Override
    public Map<String, Object> getInfo(String id) {
        UserEntity entity = userService.getInfo(id);
        return JsonUtil.entityToMap(entity);
    }
}
