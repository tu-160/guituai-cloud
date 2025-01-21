package com.future.module.system.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.future.base.service.SuperServiceImpl;
import com.future.common.util.RandomUtil;
import com.future.common.util.UserProvider;
import com.future.module.system.entity.ComFieldsEntity;
import com.future.module.system.mapper.BaseComFieldsMapper;
import com.future.module.system.service.ComFieldsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 *
 * 常用字段表
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-03-23
 */
@Service
public class ComFieldsServiceImpl extends SuperServiceImpl<BaseComFieldsMapper, ComFieldsEntity> implements ComFieldsService {

	@Autowired
    private UserProvider userProvider;


    @Override
    public List<ComFieldsEntity> getList() {
        QueryWrapper<ComFieldsEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().orderByAsc(ComFieldsEntity::getSortCode).orderByDesc(ComFieldsEntity::getCreatorTime);
        return this.list(queryWrapper);
    }

    @Override
    public ComFieldsEntity getInfo(String id) {
        QueryWrapper<ComFieldsEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ComFieldsEntity::getId, id);
        return this.getOne(queryWrapper);
    }

    @Override
    public boolean isExistByFullName(String fullName, String id) {
        QueryWrapper<ComFieldsEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ComFieldsEntity::getFieldName, fullName);
        if (!StringUtils.isEmpty(id)) {
            queryWrapper.lambda().ne(ComFieldsEntity::getId, id);
        }
        return this.count(queryWrapper) > 0 ? true : false;
    }



    @Override
    public void create(ComFieldsEntity entity) {
        entity.setId(RandomUtil.uuId());
        entity.setCreatorTime(new Date());
        entity.setCreatorUserId(userProvider.get().getUserId());
        entity.setEnabledMark(1);
        this.save(entity);
    }

    @Override
    public boolean update(String id, ComFieldsEntity entity) {
        entity.setId(id);
        entity.setLastModifyTime(new Date());
        entity.setLastModifyUserId(userProvider.get().getUserId());
        return this.updateById(entity);
    }

    @Override
    public void delete(ComFieldsEntity entity) {
        if (entity != null) {
            this.removeById(entity.getId());
        }
    }

}
