package com.future.permission.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.future.base.service.SuperServiceImpl;
import com.future.common.util.DateUtil;
import com.future.common.util.RandomUtil;
import com.future.common.util.StringUtil;
import com.future.common.util.UserProvider;
import com.future.permission.entity.ColumnsPurviewEntity;
import com.future.permission.mapper.ColumnsPurviewMapper;
import com.future.permission.service.ColumnsPurviewService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 模块列表权限业务实现类
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date ：2022/3/15 9:40
 */
@Service
public class ColumnsPurviewServiceImpl extends SuperServiceImpl<ColumnsPurviewMapper, ColumnsPurviewEntity> implements ColumnsPurviewService {
    @Autowired
    private UserProvider userProvider;

    @Override
    public ColumnsPurviewEntity getInfo(String moduleId) {
        QueryWrapper<ColumnsPurviewEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ColumnsPurviewEntity::getModuleId, moduleId);
        return this.getOne(queryWrapper);
    }

    @Override
    public boolean update(String moduleId, ColumnsPurviewEntity entity) {
        ColumnsPurviewEntity entitys = getInfo(moduleId);
        // id不存在则是保存
        if (entitys == null) {
            entity.setId(RandomUtil.uuId());
            entity.setCreatorUserId(UserProvider.getLoginUserId());
            return this.save(entity);
        } else {
            // 修改
            entity.setId(entitys.getId());
            entity.setLastModifyUserId(UserProvider.getLoginUserId());
            entity.setLastModifyTime(DateUtil.getNowDate());
        }
        return this.saveOrUpdate(entity);
    }

}
