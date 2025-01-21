package com.future.module.system.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.future.base.service.SuperServiceImpl;
import com.future.common.util.DateUtil;
import com.future.common.util.RandomUtil;
import com.future.common.util.StringUtil;
import com.future.common.util.UserProvider;
import com.future.module.system.entity.ModuleDataAuthorizeEntity;
import com.future.module.system.mapper.ModuleDataAuthorizeMapper;
import com.future.module.system.service.ModuleDataAuthorizeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 数据权限配置
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2019年9月27日 上午9:18
 */
@Service
public class ModuleDataAuthorizeServiceImpl extends SuperServiceImpl<ModuleDataAuthorizeMapper, ModuleDataAuthorizeEntity> implements ModuleDataAuthorizeService {

    @Autowired
    private UserProvider userProvider;

    @Override
    public List<ModuleDataAuthorizeEntity> getList() {
        QueryWrapper<ModuleDataAuthorizeEntity> queryWrapper = new QueryWrapper<>();
        // 排序
        queryWrapper.lambda().orderByDesc(ModuleDataAuthorizeEntity::getCreatorTime);
        return this.list(queryWrapper);
    }

    @Override
    public List<ModuleDataAuthorizeEntity> getList(String moduleId) {
        QueryWrapper<ModuleDataAuthorizeEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ModuleDataAuthorizeEntity::getModuleId, moduleId);
        // 排序
        queryWrapper.lambda().orderByDesc(ModuleDataAuthorizeEntity::getCreatorTime);
        return this.list(queryWrapper);
    }

    @Override
    public ModuleDataAuthorizeEntity getInfo(String id) {
        QueryWrapper<ModuleDataAuthorizeEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ModuleDataAuthorizeEntity::getId, id);
        return this.getOne(queryWrapper);
    }

    @Override
    public void create(ModuleDataAuthorizeEntity entity) {
//        if (StringUtil.isEmpty(entity.getId())) {
            entity.setId(RandomUtil.uuId());
            entity.setEnabledMark(1);
            entity.setSortCode(RandomUtil.parses());
//        }
        this.save(entity);
    }

    @Override
    public boolean update(String id, ModuleDataAuthorizeEntity entity) {
        entity.setId(id);
        entity.setLastModifyTime(DateUtil.getNowDate());
       return this.updateById(entity);
    }

    @Override
    public void delete(ModuleDataAuthorizeEntity entity) {
        this.removeById(entity.getId());
    }

    @Override
    @Transactional
    public boolean first(String id) {
        boolean isOk = false;
        //获取要上移的那条数据的信息
        ModuleDataAuthorizeEntity upEntity = this.getById(id);
        Long upSortCode = upEntity.getSortCode() == null ? 0 : upEntity.getSortCode();
        //查询上几条记录
        QueryWrapper<ModuleDataAuthorizeEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(ModuleDataAuthorizeEntity::getModuleId, upEntity.getModuleId())
                .lt(ModuleDataAuthorizeEntity::getSortCode, upSortCode)
                .orderByDesc(ModuleDataAuthorizeEntity::getSortCode);
        List<ModuleDataAuthorizeEntity> downEntity = this.list(queryWrapper);
        if (downEntity.size() > 0) {
            //交换两条记录的sort值
            Long temp = upEntity.getSortCode();
            upEntity.setSortCode(downEntity.get(0).getSortCode());
            downEntity.get(0).setSortCode(temp);
            updateById(downEntity.get(0));
            updateById(upEntity);
            isOk = true;
        }
        return isOk;
    }

    @Override
    @Transactional
    public boolean next(String id) {
        boolean isOk = false;
        //获取要下移的那条数据的信息
        ModuleDataAuthorizeEntity downEntity = this.getById(id);
        Long upSortCode = downEntity.getSortCode() == null ? 0 : downEntity.getSortCode();
        //查询下几条记录
        QueryWrapper<ModuleDataAuthorizeEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(ModuleDataAuthorizeEntity::getModuleId, downEntity.getModuleId())
                .gt(ModuleDataAuthorizeEntity::getSortCode, upSortCode)
                .orderByAsc(ModuleDataAuthorizeEntity::getSortCode);
        List<ModuleDataAuthorizeEntity> upEntity = this.list(queryWrapper);
        if (upEntity.size() > 0) {
            //交换两条记录的sort值
            Long temp = downEntity.getSortCode();
            downEntity.setSortCode(upEntity.get(0).getSortCode());
            upEntity.get(0).setSortCode(temp);
            updateById(upEntity.get(0));
            updateById(downEntity);
            isOk = true;
        }
        return isOk;
    }

    @Override
    public boolean isExistByEnCode(String moduleId, String enCode, String id) {
        QueryWrapper<ModuleDataAuthorizeEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ModuleDataAuthorizeEntity::getModuleId, moduleId);
        queryWrapper.lambda().eq(ModuleDataAuthorizeEntity::getEnCode, enCode);
        return this.count(queryWrapper) > 0;
    }

    @Override
    public boolean isExistByFullName(String moduleId, String fullName, String id) {
        QueryWrapper<ModuleDataAuthorizeEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ModuleDataAuthorizeEntity::getModuleId, moduleId);
        queryWrapper.lambda().eq(ModuleDataAuthorizeEntity::getFullName, fullName);
        return this.count(queryWrapper) > 0;
    }
}
