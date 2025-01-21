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
import com.future.module.system.entity.ModuleButtonEntity;
import com.future.module.system.entity.ModuleColumnEntity;
import com.future.module.system.mapper.ModuleColumnMapper;
import com.future.module.system.service.ModuleColumnService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 列表权限
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2019年9月27日 上午9:18
 */
@Service
public class ModuleColumnServiceImpl extends SuperServiceImpl<ModuleColumnMapper, ModuleColumnEntity> implements ModuleColumnService {

    @Autowired
    private UserProvider userProvider;

    @Override
    public List<ModuleColumnEntity> getList() {
        QueryWrapper<ModuleColumnEntity> queryWrapper = new QueryWrapper<>();
        // 排序
        queryWrapper.lambda().orderByAsc(ModuleColumnEntity::getSortCode)
                .orderByDesc(ModuleColumnEntity::getCreatorTime);
        return this.list(queryWrapper);
    }

    @Override
    public List<ModuleColumnEntity> getEnabledMarkList(String enabledMark) {
        QueryWrapper<ModuleColumnEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ModuleColumnEntity::getEnabledMark,enabledMark);
        // 排序
        queryWrapper.lambda().orderByAsc(ModuleColumnEntity::getSortCode)
                .orderByDesc(ModuleColumnEntity::getCreatorTime);
        return this.list(queryWrapper);
    }

    @Override
    public List<ModuleColumnEntity> getList(String moduleId) {
        QueryWrapper<ModuleColumnEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ModuleColumnEntity::getModuleId, moduleId);
        // 排序
        queryWrapper.lambda().orderByAsc(ModuleColumnEntity::getSortCode)
                .orderByDesc(ModuleColumnEntity::getCreatorTime);
        return this.list(queryWrapper);
    }

    @Override
    public List<ModuleColumnEntity> getList(String moduleId, Pagination pagination) {
        // 定义变量判断是否需要使用修改时间倒序
        boolean flag = false;
        QueryWrapper<ModuleColumnEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ModuleColumnEntity::getModuleId, moduleId);
        if(!StringUtil.isEmpty(pagination.getKeyword())){
            flag = true;
            queryWrapper.lambda().and(
                    t-> t.like(ModuleColumnEntity::getEnCode,pagination.getKeyword()).or().like(ModuleColumnEntity::getFullName,pagination.getKeyword())
            );
        }
        // 排序
        queryWrapper.lambda().orderByAsc(ModuleColumnEntity::getSortCode)
                .orderByDesc(ModuleColumnEntity::getCreatorTime);
        if (flag) {
            queryWrapper.lambda().orderByDesc(ModuleColumnEntity::getLastModifyTime);
        }
        return this.list(queryWrapper);
    }


    @Override
    public List<ModuleColumnEntity> getListByBindTable(String bindTable) {
        QueryWrapper<ModuleColumnEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ModuleColumnEntity::getBindTable, bindTable)
                .orderByAsc(ModuleColumnEntity::getSortCode).orderByDesc(ModuleColumnEntity::getCreatorTime);
        List<ModuleColumnEntity> list = this.list(queryWrapper);
        return list;
    }

    @Override
    public ModuleColumnEntity getInfo(String id) {
        QueryWrapper<ModuleColumnEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ModuleColumnEntity::getId, id);
        return this.getOne(queryWrapper);
    }

    @Override
    public ModuleColumnEntity getInfo(String id, String moduleId) {
        QueryWrapper<ModuleColumnEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ModuleColumnEntity::getId, id);
        queryWrapper.lambda().eq(ModuleColumnEntity::getModuleId, moduleId);
        return this.getOne(queryWrapper);
    }

    @Override
    public boolean isExistByFullName(String moduleId, String fullName, String id) {
        QueryWrapper<ModuleColumnEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ModuleColumnEntity::getFullName, fullName).eq(ModuleColumnEntity::getModuleId, moduleId);
        if (!StringUtil.isEmpty(id)) {
            queryWrapper.lambda().ne(ModuleColumnEntity::getId, id);
        }
        return this.count(queryWrapper) > 0 ? true : false;
    }

    @Override
    public boolean isExistByEnCode(String moduleId, String enCode, String id) {
        QueryWrapper<ModuleColumnEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ModuleColumnEntity::getEnCode, enCode).eq(ModuleColumnEntity::getModuleId, moduleId);
        if (!StringUtil.isEmpty(id)) {
            queryWrapper.lambda().ne(ModuleColumnEntity::getId, id);
        }
        return this.count(queryWrapper) > 0 ? true : false;
    }

    @Override
    public void create(ModuleColumnEntity entity) {
//        if (entity.getId() == null) {
//            entity.setSortCode(entity.getSortCode());
            entity.setId(RandomUtil.uuId());
//        }
        this.save(entity);
    }

    @Override
    public void create(List<ModuleColumnEntity> entitys) {
        Long sortCode = RandomUtil.parses();
        String userId = userProvider.get().getUserId();
        for (ModuleColumnEntity entity : entitys) {
            entity.setId(RandomUtil.uuId());
            entity.setSortCode(sortCode++);
            entity.setEnabledMark("1".equals(String.valueOf(entity.getEnabledMark()))?0:1);
            entity.setCreatorUserId(userId);
            this.save(entity);
        }
    }

    @Override
    public boolean update(String id, ModuleColumnEntity entity) {
        entity.setId(id);
        entity.setLastModifyTime(DateUtil.getNowDate());
        return this.updateById(entity);
    }

    @Override
    public void delete(ModuleColumnEntity entity) {
        this.removeById(entity.getId());
    }

    @Override
    public boolean first(String id) {
        boolean isOk = false;
        //获取要上移的那条数据的信息
        ModuleColumnEntity upEntity = this.getById(id);
        Long upSortCode = upEntity.getSortCode() == null ? 0 : upEntity.getSortCode();
        //查询上几条记录
        QueryWrapper<ModuleColumnEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(ModuleColumnEntity::getModuleId, upEntity.getModuleId())
                .eq(ModuleColumnEntity::getBindTable, upEntity.getBindTable())
                .lt(ModuleColumnEntity::getSortCode, upSortCode)
                .orderByDesc(ModuleColumnEntity::getSortCode);
        List<ModuleColumnEntity> downEntity = this.list(queryWrapper);
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
    public boolean next(String id) {
        boolean isOk = false;
        //获取要下移的那条数据的信息
        ModuleColumnEntity downEntity = this.getById(id);
        Long upSortCode = downEntity.getSortCode() == null ? 0 : downEntity.getSortCode();
        //查询下几条记录
        QueryWrapper<ModuleColumnEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(ModuleColumnEntity::getModuleId, downEntity.getModuleId())
                .eq(ModuleColumnEntity::getBindTable, downEntity.getBindTable())
                .gt(ModuleColumnEntity::getSortCode, upSortCode)
                .orderByAsc(ModuleColumnEntity::getSortCode);
        List<ModuleColumnEntity> upEntity = this.list(queryWrapper);
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
    public List<ModuleColumnEntity> getListByModuleId(List<String> ids) {
        if (ids.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        QueryWrapper<ModuleColumnEntity> queryWrapper = new QueryWrapper<>();
        List<List<String>> lists = Lists.partition(ids, 1000);
        for (List<String> list : lists) {
            queryWrapper.lambda().or().in(ModuleColumnEntity::getModuleId, list);
        }
        queryWrapper.lambda().eq(ModuleColumnEntity::getEnabledMark, 1);
        queryWrapper.lambda().orderByAsc(ModuleColumnEntity::getSortCode).orderByDesc(ModuleColumnEntity::getCreatorTime);
        return this.list(queryWrapper);
    }

    @Override
    public List<ModuleColumnEntity> getListByIds(List<String> ids) {
        if (ids.size() == 0) {
            return new ArrayList<>();
        }
        QueryWrapper<ModuleColumnEntity> queryWrapper = new QueryWrapper<>();
        List<List<String>> lists = Lists.partition(ids, 1000);
        for (List<String> list : lists) {
            queryWrapper.lambda().or().in(ModuleColumnEntity::getId, list);
        }
        queryWrapper.lambda().eq(ModuleColumnEntity::getEnabledMark, 1);
        return this.list(queryWrapper);
    }
}
