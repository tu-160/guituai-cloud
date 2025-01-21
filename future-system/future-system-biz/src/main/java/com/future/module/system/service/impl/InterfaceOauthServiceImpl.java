package com.future.module.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.future.base.service.SuperServiceImpl;
import com.future.common.exception.DataException;
import com.future.common.util.DateUtil;
import com.future.common.util.RandomUtil;
import com.future.common.util.StringUtil;
import com.future.common.util.UserProvider;
import com.future.module.system.entity.InterfaceOauthEntity;
import com.future.module.system.mapper.InterfaceOauthMapper;
import com.future.module.system.model.InterfaceOauth.PaginationOauth;
import com.future.module.system.service.InterfaceOauthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 接口认证服务
 *
 * @author Future Platform Group
 * @version v4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2022/6/8 9:50
 */
@Service
public class InterfaceOauthServiceImpl extends SuperServiceImpl<InterfaceOauthMapper, InterfaceOauthEntity> implements InterfaceOauthService {

    @Autowired
    private UserProvider userProvider;


    @Override
    public boolean isExistByAppName(String appName, String id) {
        QueryWrapper<InterfaceOauthEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(InterfaceOauthEntity::getAppName, appName);
        if (!StringUtil.isEmpty(id)) {
            queryWrapper.lambda().ne(InterfaceOauthEntity::getId, id);
        }
        return this.count(queryWrapper) > 0 ? true : false;
    }

    @Override
    public boolean isExistByAppId(String appId, String id) {
        QueryWrapper<InterfaceOauthEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(InterfaceOauthEntity::getAppId, appId);
        if (!StringUtil.isEmpty(id)) {
            queryWrapper.lambda().ne(InterfaceOauthEntity::getId, id);
        }
        return this.count(queryWrapper) > 0 ? true : false;
    }

    @Override
    public List<InterfaceOauthEntity> getList(PaginationOauth pagination) {
        boolean flag = false;
        QueryWrapper<InterfaceOauthEntity> queryWrapper = new QueryWrapper<>();
        //查询关键字
        if (StringUtil.isNotEmpty(pagination.getKeyword())) {
            flag = true;
            queryWrapper.lambda().and(
                    t -> t.like(InterfaceOauthEntity::getAppId, pagination.getKeyword())
                            .or().like(InterfaceOauthEntity::getAppName, pagination.getKeyword())
            );
        }
        if (pagination.getEnabledMark() != null) {
            queryWrapper.lambda().eq(InterfaceOauthEntity::getEnabledMark, pagination.getEnabledMark());
        }
        //排序
        queryWrapper.lambda().orderByAsc(InterfaceOauthEntity::getSortCode)
                .orderByDesc(InterfaceOauthEntity::getCreatorTime);
        if (flag) {
            queryWrapper.lambda().orderByDesc(InterfaceOauthEntity::getLastModifyTime);
        }
        Page<InterfaceOauthEntity> page = new Page<>(pagination.getCurrentPage(), pagination.getPageSize());
        IPage<InterfaceOauthEntity> iPage = this.page(page, queryWrapper);
        return pagination.setData(iPage.getRecords(), iPage.getTotal());
    }

    @Override
    public InterfaceOauthEntity getInfo(String id) {
        QueryWrapper<InterfaceOauthEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(InterfaceOauthEntity::getId, id);
        return this.getOne(queryWrapper);
    }

    @Override
    public void create(InterfaceOauthEntity entity) {
        if (entity.getId() == null) {
            entity.setId(RandomUtil.uuId());
            entity.setCreatorUserId(userProvider.get().getUserId());
            entity.setCreatorTime(DateUtil.getNowDate());
            entity.setLastModifyTime(DateUtil.getNowDate());
        }
        this.save(entity);
    }

    @Override
    public boolean update(InterfaceOauthEntity entity, String id) throws DataException {
        entity.setId(id);
        entity.setLastModifyUserId(userProvider.get().getUserId());
        entity.setLastModifyTime(DateUtil.getNowDate());
        return this.updateById(entity);
    }

    @Override
    public void delete(InterfaceOauthEntity entity) {
        this.removeById(entity.getId());
    }

    @Override
    public InterfaceOauthEntity getInfoByAppId(String appId) {
        QueryWrapper<InterfaceOauthEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(InterfaceOauthEntity::getAppId, appId);
        return this.getOne(queryWrapper);
    }
}
