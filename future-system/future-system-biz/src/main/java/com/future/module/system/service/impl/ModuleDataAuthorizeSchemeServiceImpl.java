package com.future.module.system.service.impl;


import com.google.common.collect.Lists;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.future.base.service.SuperServiceImpl;
import com.future.common.util.DateUtil;
import com.future.common.util.RandomUtil;
import com.future.common.util.StringUtil;
import com.future.common.util.UserProvider;
import com.future.module.system.entity.ModuleDataAuthorizeSchemeEntity;
import com.future.module.system.entity.ModuleFormEntity;
import com.future.module.system.mapper.ModuleDataAuthorizeSchemeMapper;
import com.future.module.system.service.ModuleDataAuthorizeSchemeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 数据权限方案
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2019年9月27日 上午9:18
 */
@Service
public class ModuleDataAuthorizeSchemeServiceImpl extends SuperServiceImpl<ModuleDataAuthorizeSchemeMapper, ModuleDataAuthorizeSchemeEntity> implements ModuleDataAuthorizeSchemeService {

    @Autowired
    private UserProvider userProvider;

    @Override
    public List<ModuleDataAuthorizeSchemeEntity> getList() {
        QueryWrapper<ModuleDataAuthorizeSchemeEntity> queryWrapper = new QueryWrapper<>();
        // 排序
        queryWrapper.lambda().orderByDesc(ModuleDataAuthorizeSchemeEntity::getCreatorTime);
        return this.list(queryWrapper);
    }

    @Override
    public List<ModuleDataAuthorizeSchemeEntity> getEnabledMarkList(String enabledMark) {
        QueryWrapper<ModuleDataAuthorizeSchemeEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ModuleDataAuthorizeSchemeEntity::getEnabledMark,enabledMark);
        // 排序
        queryWrapper.lambda().orderByDesc(ModuleDataAuthorizeSchemeEntity::getCreatorTime);
        return this.list(queryWrapper);
    }

    @Override
    public List<ModuleDataAuthorizeSchemeEntity> getList(String moduleId) {
        QueryWrapper<ModuleDataAuthorizeSchemeEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ModuleDataAuthorizeSchemeEntity::getModuleId, moduleId);
        // 排序
        queryWrapper.lambda().orderByDesc(ModuleDataAuthorizeSchemeEntity::getCreatorTime);
        return this.list(queryWrapper);
    }

    @Override
    public ModuleDataAuthorizeSchemeEntity getInfo(String id) {
        QueryWrapper<ModuleDataAuthorizeSchemeEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ModuleDataAuthorizeSchemeEntity::getId, id);
        return this.getOne(queryWrapper);
    }

    @Override
    public void create(ModuleDataAuthorizeSchemeEntity entity) {
//        if (StringUtil.isEmpty(entity.getId())) {
            entity.setId(RandomUtil.uuId());
            entity.setSortCode(RandomUtil.parses());
            entity.setEnabledMark(1);
//        }
        this.save(entity);
    }

    @Override
    public boolean update(String id, ModuleDataAuthorizeSchemeEntity entity) {
        entity.setId(id);
        entity.setEnabledMark(1);
        entity.setLastModifyTime(DateUtil.getNowDate());
        return  this.updateById(entity);
    }

    @Override
    public void delete(ModuleDataAuthorizeSchemeEntity entity) {
        this.removeById(entity.getId());
    }

    @Override
    @Transactional
    public boolean first(String id) {
        boolean isOk = false;
        //获取要上移的那条数据的信息
        ModuleDataAuthorizeSchemeEntity upEntity = this.getById(id);
        Long upSortCode = upEntity.getSortCode() == null ? 0 : upEntity.getSortCode();
        //查询上几条记录
        QueryWrapper<ModuleDataAuthorizeSchemeEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .lt(ModuleDataAuthorizeSchemeEntity::getSortCode, upSortCode)
                .orderByDesc(ModuleDataAuthorizeSchemeEntity::getSortCode);
        List<ModuleDataAuthorizeSchemeEntity> downEntity = this.list(queryWrapper);
        if (downEntity.size() > 0) {
            //交换两条记录的sort值
            Long temp = upEntity.getSortCode();
            upEntity.setSortCode(downEntity.get(0).getSortCode());
            downEntity.get(0).setSortCode(temp);
            this.updateById(downEntity.get(0));
            this.updateById(upEntity);
            isOk = true;
        }
        return isOk;
    }

    @Override
    @Transactional
    public boolean next(String id) {
        boolean isOk = false;
        //获取要下移的那条数据的信息
        ModuleDataAuthorizeSchemeEntity downEntity = this.getById(id);
        Long upSortCode = downEntity.getSortCode() == null ? 0 : downEntity.getSortCode();
        //查询下几条记录
        QueryWrapper<ModuleDataAuthorizeSchemeEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .gt(ModuleDataAuthorizeSchemeEntity::getSortCode, upSortCode)
                .orderByAsc(ModuleDataAuthorizeSchemeEntity::getSortCode);
        List<ModuleDataAuthorizeSchemeEntity> upEntity = this.list(queryWrapper);
        if (upEntity.size() > 0) {
            //交换两条记录的sort值
            Long temp = downEntity.getSortCode();
            downEntity.setSortCode(upEntity.get(0).getSortCode());
            upEntity.get(0).setSortCode(temp);
            this.updateById(upEntity.get(0));
            this.updateById(downEntity);
            isOk = true;
        }
        return isOk;
    }

    @Override
    public Boolean isExistByEnCode(String id, String enCode, String moduleId) {
        QueryWrapper<ModuleDataAuthorizeSchemeEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ModuleDataAuthorizeSchemeEntity::getModuleId, moduleId);
        queryWrapper.lambda().eq(ModuleDataAuthorizeSchemeEntity::getEnCode, enCode);
        if (StringUtil.isNotEmpty(id)) {
            queryWrapper.lambda().ne(ModuleDataAuthorizeSchemeEntity::getId, id);
        }
        return this.count(queryWrapper) > 0;
    }

    @Override
    public Boolean isExistByFullName(String id, String fullName, String moduleId) {
        QueryWrapper<ModuleDataAuthorizeSchemeEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ModuleDataAuthorizeSchemeEntity::getModuleId, moduleId);
        queryWrapper.lambda().eq(ModuleDataAuthorizeSchemeEntity::getFullName, fullName);
        if (StringUtil.isNotEmpty(id)) {
            queryWrapper.lambda().ne(ModuleDataAuthorizeSchemeEntity::getId, id);
        }
        return this.count(queryWrapper) > 0;
    }
    @Override
    public Boolean isExistAllData(String moduleId) {
        QueryWrapper<ModuleDataAuthorizeSchemeEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ModuleDataAuthorizeSchemeEntity::getModuleId, moduleId);
        queryWrapper.lambda().eq(ModuleDataAuthorizeSchemeEntity::getAllData, 1);
        return this.count(queryWrapper) > 0;
    }

    @Override
    public List<ModuleDataAuthorizeSchemeEntity> getListByModuleId(List<String> ids) {
        if (ids.size() == 0) {
            return new ArrayList<>();
        }
        QueryWrapper<ModuleDataAuthorizeSchemeEntity> queryWrapper = new QueryWrapper<>();
        List<List<String>> lists = Lists.partition(ids, 1000);
        for (List<String> list : lists) {
            queryWrapper.lambda().or().in(ModuleDataAuthorizeSchemeEntity::getModuleId, list);
        }
        queryWrapper.lambda().eq(ModuleDataAuthorizeSchemeEntity::getEnabledMark, 1);
        queryWrapper.lambda().orderByAsc(ModuleDataAuthorizeSchemeEntity::getSortCode).orderByDesc(ModuleDataAuthorizeSchemeEntity::getCreatorTime);
        return this.list(queryWrapper);
    }

    @Override
    public List<ModuleDataAuthorizeSchemeEntity> getListByIds(List<String> ids) {
        if (ids.size() == 0) {
            return new ArrayList<>();
        }
        QueryWrapper<ModuleDataAuthorizeSchemeEntity> queryWrapper = new QueryWrapper<>();
        List<List<String>> lists = Lists.partition(ids, 1000);
        for (List<String> list : lists) {
            queryWrapper.lambda().or().in(ModuleDataAuthorizeSchemeEntity::getId, list);
        }
        queryWrapper.lambda().eq(ModuleDataAuthorizeSchemeEntity::getEnabledMark, 1);
        return this.list(queryWrapper);
    }
}
