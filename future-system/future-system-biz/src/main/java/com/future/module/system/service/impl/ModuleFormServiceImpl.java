package com.future.module.system.service.impl;

import com.google.common.collect.Lists;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.future.base.service.SuperServiceImpl;
import com.future.common.base.Pagination;
import com.future.common.util.DateUtil;
import com.future.common.util.RandomUtil;
import com.future.common.util.StringUtil;
import com.future.common.util.UserProvider;
import com.future.module.system.entity.ModuleColumnEntity;
import com.future.module.system.entity.ModuleFormEntity;
import com.future.module.system.mapper.ModuleFormMapper;
import com.future.module.system.service.ModuleFormService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 表单权限
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2019年9月27日 上午9:18
 */
@Service
public class ModuleFormServiceImpl extends SuperServiceImpl<ModuleFormMapper, ModuleFormEntity> implements ModuleFormService {

    @Autowired
    private UserProvider userProvider;

    @Override
    public List<ModuleFormEntity> getList() {
        QueryWrapper<ModuleFormEntity> queryWrapper = new QueryWrapper<>();
        // 排序
        queryWrapper.lambda().orderByAsc(ModuleFormEntity::getSortCode)
                .orderByDesc(ModuleFormEntity::getCreatorTime);
        return this.list(queryWrapper);
    }

    @Override
    public List<ModuleFormEntity> getEnabledMarkList(String enabledMark) {
        QueryWrapper<ModuleFormEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ModuleFormEntity::getEnabledMark,enabledMark);
        // 排序
        queryWrapper.lambda().orderByAsc(ModuleFormEntity::getSortCode)
                .orderByDesc(ModuleFormEntity::getCreatorTime);
        return this.list(queryWrapper);
    }

    @Override
    public List<ModuleFormEntity> getList(String moduleId, Pagination pagination) {
        // 定义变量判断是否需要使用修改时间倒序
        boolean flag = false;
        QueryWrapper<ModuleFormEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ModuleFormEntity::getModuleId, moduleId);
        if(!StringUtil.isEmpty(pagination.getKeyword())){
            flag = true;
            queryWrapper.lambda().and(
                    t-> t.like(ModuleFormEntity::getEnCode,pagination.getKeyword()).or().like(ModuleFormEntity::getFullName,pagination.getKeyword())
            );
        }
        // 排序
        queryWrapper.lambda().orderByAsc(ModuleFormEntity::getSortCode)
                .orderByDesc(ModuleFormEntity::getCreatorTime);
        if (flag) {
            queryWrapper.lambda().orderByDesc(ModuleFormEntity::getLastModifyTime);
        }
        return this.list(queryWrapper);
    }

    @Override
    public List<ModuleFormEntity> getList(String moduleId) {
        QueryWrapper<ModuleFormEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ModuleFormEntity::getModuleId, moduleId);
        // 排序
        queryWrapper.lambda().orderByAsc(ModuleFormEntity::getSortCode)
                .orderByDesc(ModuleFormEntity::getCreatorTime);
        return this.list(queryWrapper);
    }

    @Override
    public ModuleFormEntity getInfo(String id) {
        QueryWrapper<ModuleFormEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ModuleFormEntity::getId, id);
        return this.getOne(queryWrapper);
    }

    @Override
    public ModuleFormEntity getInfo(String id, String moduleId) {
        QueryWrapper<ModuleFormEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ModuleFormEntity::getId, id);
        queryWrapper.lambda().eq(ModuleFormEntity::getModuleId, moduleId);
        return this.getOne(queryWrapper);
    }

    @Override
    public boolean isExistByFullName(String moduleId, String fullName, String id) {
        QueryWrapper<ModuleFormEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ModuleFormEntity::getFullName, fullName).eq(ModuleFormEntity::getModuleId, moduleId);
        if (!StringUtil.isEmpty(id)) {
            queryWrapper.lambda().ne(ModuleFormEntity::getId, id);
        }
        return this.count(queryWrapper) > 0 ? true : false;
    }

    @Override
    public boolean isExistByEnCode(String moduleId, String enCode, String id) {
        QueryWrapper<ModuleFormEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ModuleFormEntity::getEnCode, enCode).eq(ModuleFormEntity::getModuleId, moduleId);
        if (!StringUtil.isEmpty(id)) {
            queryWrapper.lambda().ne(ModuleFormEntity::getId, id);
        }
        return this.count(queryWrapper) > 0 ? true : false;
    }

    @Override
    public void create(ModuleFormEntity entity) {
//        if (entity.getId() == null) {
            entity.setId(RandomUtil.uuId());
//        }
        this.save(entity);
    }

    @Override
    @Transactional
    public void create(List<ModuleFormEntity> entitys) {
        Long sortCode = RandomUtil.parses();
        String userId = userProvider.get().getUserId();
        for (ModuleFormEntity entity : entitys) {
            entity.setId(RandomUtil.uuId());
            entity.setSortCode(sortCode++);
            entity.setEnabledMark("1".equals(String.valueOf(entity.getEnabledMark()))?0:1);
            entity.setCreatorUserId(userId);
            this.save(entity);
        }
    }

    @Override
    public boolean update(String id, ModuleFormEntity entity) {
        entity.setId(id);
        entity.setLastModifyTime(DateUtil.getNowDate());
        return this.updateById(entity);
    }

    @Override
    public void delete(ModuleFormEntity entity) {
        this.removeById(entity.getId());
    }

    @Override
    public List<ModuleFormEntity> getListByModuleId(List<String> ids) {
        if (ids.size() == 0) {
            return new ArrayList<>();
        }
        QueryWrapper<ModuleFormEntity> queryWrapper = new QueryWrapper<>();
        List<List<String>> lists = Lists.partition(ids, 1000);
        for (List<String> list : lists) {
            queryWrapper.lambda().or().in(ModuleFormEntity::getModuleId, list);
        }
        queryWrapper.lambda().eq(ModuleFormEntity::getEnabledMark, 1);
        queryWrapper.lambda().orderByAsc(ModuleFormEntity::getSortCode).orderByDesc(ModuleFormEntity::getCreatorTime);
        return this.list(queryWrapper);
    }

    @Override
    public List<ModuleFormEntity> getListByIds(List<String> ids) {
        if (ids.size() == 0) {
            return new ArrayList<>();
        }
        QueryWrapper<ModuleFormEntity> queryWrapper = new QueryWrapper<>();
        List<List<String>> lists = Lists.partition(ids, 1000);
        for (List<String> list : lists) {
            queryWrapper.lambda().or().in(ModuleFormEntity::getId, list);
        }
        queryWrapper.lambda().eq(ModuleFormEntity::getEnabledMark, 1);
        return this.list(queryWrapper);
    }


}
