package com.future.module.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.future.base.service.SuperServiceImpl;
import com.future.common.base.Pagination;
import com.future.common.util.DateUtil;
import com.future.common.util.RandomUtil;
import com.future.common.util.StringUtil;
import com.future.common.util.UserProvider;
import com.future.module.system.entity.VisualDataMapEntity;
import com.future.module.system.mapper.VisualDataMapMapper;
import com.future.module.system.service.VisualDataMapService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 大屏地图
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2019年9月26日 上午9:18
 */
@Service
public class VisualDataMapServiceImpl extends SuperServiceImpl<VisualDataMapMapper, VisualDataMapEntity> implements VisualDataMapService {

	@Autowired
    private UserProvider userProvider;

    @Override
    public List<VisualDataMapEntity> getList(Pagination pagination){
        // 定义变量判断是否需要使用修改时间倒序
        boolean flag = false;
        QueryWrapper<VisualDataMapEntity> queryWrapper = new QueryWrapper<>();
        if (StringUtil.isNotEmpty(pagination.getKeyword())) {
            flag = true;
            queryWrapper.lambda().and(
                    t -> t.like(VisualDataMapEntity::getFullName, pagination.getKeyword())
                            .or().like(VisualDataMapEntity::getEnCode, pagination.getKeyword())
            );
        }
        //排序
        queryWrapper.lambda().orderByAsc(VisualDataMapEntity::getSortCode)
                .orderByDesc(VisualDataMapEntity::getCreatorTime);
        if (flag) {
            queryWrapper.lambda().orderByDesc(VisualDataMapEntity::getLastModifyTime);
        }
        Page page = new Page(pagination.getCurrentPage(), pagination.getPageSize());
        IPage<VisualDataMapEntity> IPages = this.page(page, queryWrapper);
        return pagination.setData(IPages.getRecords(), page.getTotal());
    }

    @Override
    public List<VisualDataMapEntity> getList() {
        QueryWrapper<VisualDataMapEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().orderByDesc(VisualDataMapEntity::getSortCode)
                .orderByDesc(VisualDataMapEntity::getCreatorTime);
        return this.list(queryWrapper);
    }

    @Override
    public VisualDataMapEntity getInfo(String id) {
        QueryWrapper<VisualDataMapEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(VisualDataMapEntity::getId, id);
        return this.getOne(queryWrapper);
    }

    @Override
    public void create(VisualDataMapEntity entity) {
        entity.setId(RandomUtil.uuId());
        entity.setCreatorTime(DateUtil.getNowDate());
        entity.setCreatorUser(userProvider.get().getUserId());
        entity.setEnabledMark(1);
        this.save(entity);
    }

    @Override
    public boolean update(String id, VisualDataMapEntity entity) {
        entity.setId(id);
        entity.setLastModifyTime(DateUtil.getNowDate());
        entity.setLastModifyUser(userProvider.get().getUserId());
        return this.updateById(entity);
    }

    @Override
    public void delete(VisualDataMapEntity entity) {
        if (entity != null) {
            this.removeById(entity.getId());
        }
    }

    @Override
    public boolean isExistByFullName(String fullName, String id) {
        QueryWrapper<VisualDataMapEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(VisualDataMapEntity::getFullName, fullName);
        if (!StringUtils.isEmpty(id)) {
            queryWrapper.lambda().ne(VisualDataMapEntity::getId, id);
        }
        return this.count(queryWrapper) > 0 ? true : false;
    }

    @Override
    public boolean isExistByEnCode(String enCode, String id) {
        QueryWrapper<VisualDataMapEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(VisualDataMapEntity::getEnCode, enCode);
        if (!StringUtils.isEmpty(id)) {
            queryWrapper.lambda().ne(VisualDataMapEntity::getId, id);
        }
        return this.count(queryWrapper) > 0 ? true : false;
    }

}
