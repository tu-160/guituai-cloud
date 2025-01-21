package com.future.permission.service.impl;

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
import com.future.permission.entity.GroupEntity;
import com.future.permission.mapper.GroupMapper;
import com.future.permission.model.usergroup.PaginationGroup;
import com.future.permission.service.GroupService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 分组管理业务类实现类
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date ：2022/3/10 18:00
 */
@Service
public class GroupServiceImpl extends SuperServiceImpl<GroupMapper, GroupEntity> implements GroupService {

    @Autowired
    private UserProvider userProvider;

    @Override
    public List<GroupEntity> getList(PaginationGroup pagination) {
        QueryWrapper<GroupEntity> queryWrapper = new QueryWrapper<>();
        // 定义变量判断是否需要使用修改时间倒序
        boolean flag = false;
        // 判断关键字
        String keyword = pagination.getKeyword();
        if (StringUtil.isNotEmpty(keyword)) {
            flag = true;
            queryWrapper.lambda().and(
                    t -> t.like(GroupEntity::getFullName, keyword)
                            .or().like(GroupEntity::getEnCode, keyword)
                            .or().like(GroupEntity::getDescription, keyword)
            );
        }
        if (pagination.getEnabledMark() != null) {
            queryWrapper.lambda().eq(GroupEntity::getEnabledMark, pagination.getEnabledMark());
        }
        if (StringUtil.isNotEmpty(pagination.getType())) {
            flag = true;
            queryWrapper.lambda().eq(GroupEntity::getType, pagination.getType());
        }
        // 获取列表
        queryWrapper.lambda().orderByAsc(GroupEntity::getSortCode).orderByDesc(GroupEntity::getCreatorTime);
        if (flag) {
            queryWrapper.lambda().orderByDesc(GroupEntity::getLastModifyTime);
        }
        Page<GroupEntity> page = new Page<>(pagination.getCurrentPage(), pagination.getPageSize());
        IPage<GroupEntity> iPage = this.page(page, queryWrapper);
        return pagination.setData(iPage.getRecords(), iPage.getTotal());
    }

    @Override
    public List<GroupEntity> list() {
        QueryWrapper<GroupEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(GroupEntity::getEnabledMark, 1);
        queryWrapper.lambda().orderByAsc(GroupEntity::getSortCode).orderByDesc(GroupEntity::getCreatorTime);
        return this.list(queryWrapper);
    }

    @Override
    public Map<String, Object> getGroupMap() {
        QueryWrapper<GroupEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().select(GroupEntity::getId,GroupEntity::getFullName);
        return this.list(queryWrapper).parallelStream().collect(Collectors.toMap(GroupEntity::getId,GroupEntity::getFullName));
    }

    @Override
    public Map<String, Object> getGroupEncodeMap() {
        QueryWrapper<GroupEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().select(GroupEntity::getId,GroupEntity::getFullName,GroupEntity::getEnCode);
        return this.list(queryWrapper).stream().collect(Collectors.toMap(group->group.getFullName() + "/" + group.getEnCode(),GroupEntity::getId));
    }

    @Override
    public GroupEntity getInfo(String id) {
        QueryWrapper<GroupEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(GroupEntity::getId, id);
        return this.getOne(queryWrapper);
    }

    @Override
    public void crete(GroupEntity entity) {
        entity.setId(RandomUtil.uuId());
        entity.setCreatorUserId(UserProvider.getLoginUserId());
        entity.setCreatorTime(DateUtil.getNowDate());
        entity.setLastModifyTime(DateUtil.getNowDate());
        this.save(entity);
    }

    @Override
    public Boolean update(String id, GroupEntity entity) {
        entity.setId(id);
        entity.setLastModifyUserId(UserProvider.getLoginUserId());
        entity.setLastModifyTime(DateUtil.getNowDate());
        return this.updateById(entity);
    }

    @Override
    public void delete(GroupEntity entity) {
        this.removeById(entity.getId());
    }

    @Override
    public Boolean isExistByFullName(String fullName, String id) {
        QueryWrapper<GroupEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(GroupEntity::getFullName, fullName);
        if (!StringUtil.isEmpty(id)) {
            queryWrapper.lambda().ne(GroupEntity::getId, id);
        }
        return this.count(queryWrapper) > 0;
    }

    @Override
    public Boolean isExistByEnCode(String enCode, String id) {
        QueryWrapper<GroupEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(GroupEntity::getEnCode, enCode);
        if (!StringUtil.isEmpty(id)) {
            queryWrapper.lambda().ne(GroupEntity::getId, id);
        }
        return this.count(queryWrapper) > 0;
    }

    @Override
    public List<GroupEntity> getListByIds(List<String> list, Boolean filterEnabledMark) {
        if (list.size() == 0) {
            return new ArrayList<>();
        }
        QueryWrapper<GroupEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(GroupEntity::getId, list);
        if (filterEnabledMark) {
            queryWrapper.lambda().eq(GroupEntity::getEnabledMark, 1);
        }
        return this.list(queryWrapper);
    }

}
