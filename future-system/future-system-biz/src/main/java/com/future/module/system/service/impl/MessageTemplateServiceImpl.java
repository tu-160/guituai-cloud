package com.future.module.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.future.base.service.SuperServiceImpl;
import com.future.common.base.Pagination;
import com.future.common.util.RandomUtil;
import com.future.common.util.StringUtil;
import com.future.common.util.UserProvider;
import com.future.module.system.entity.MessageTemplateEntity;
import com.future.module.system.mapper.MessageTemplateMapper;
import com.future.module.system.service.MessageTemplateService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 消息模板
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021年12月8日17:40:37
 */
@Service
public class MessageTemplateServiceImpl extends SuperServiceImpl<MessageTemplateMapper, MessageTemplateEntity> implements MessageTemplateService {

    @Autowired
    private UserProvider userProvider;

    @Override
    public List<MessageTemplateEntity> getList() {
        QueryWrapper<MessageTemplateEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(MessageTemplateEntity::getEnabledMark, 1);
        queryWrapper.lambda().orderByDesc(MessageTemplateEntity::getCreatorTime);
        return this.list(queryWrapper);
    }

    @Override
    public List<MessageTemplateEntity> getList(Pagination pagination, Boolean filter) {
        // 定义变量判断是否需要使用修改时间倒序
        boolean flag = false;
        QueryWrapper<MessageTemplateEntity> queryWrapper = new QueryWrapper<>();
        if (!StringUtil.isEmpty(pagination.getKeyword())) {
            flag = true;
            queryWrapper.lambda().and(
                    t -> t.like(MessageTemplateEntity::getFullName, pagination.getKeyword())
                        .or().like(MessageTemplateEntity::getTitle, pagination.getKeyword())
                        .or().like(MessageTemplateEntity::getEnCode, pagination.getKeyword())
            );
        }
        if (filter) {
            queryWrapper.lambda().eq(MessageTemplateEntity::getEnabledMark, 1);
        }
        queryWrapper.lambda().orderByDesc(MessageTemplateEntity::getCreatorTime);
        if (flag) {
            queryWrapper.lambda().orderByDesc(MessageTemplateEntity::getLastModifyTime);
        }
        Page<MessageTemplateEntity> page = new Page<>(pagination.getCurrentPage(), pagination.getPageSize());
        IPage<MessageTemplateEntity> userPage = this.page(page, queryWrapper);
        return pagination.setData(userPage.getRecords(), page.getTotal());
    }

    @Override
    public MessageTemplateEntity getInfo(String id) {
        QueryWrapper<MessageTemplateEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(MessageTemplateEntity::getId, id);
        return this.getOne(queryWrapper);
    }

    @Override
    @Transactional
    public void create(MessageTemplateEntity entity) {
        entity.setId(RandomUtil.uuId());
        entity.setCreatorUserId(userProvider.get().getUserId());
        this.save(entity);
    }

    @Override
    @Transactional
    public boolean update(String id, MessageTemplateEntity entity) {
        entity.setId(id);
        entity.setLastModifyTime(new Date());
        entity.setLastModifyUserId(userProvider.get().getUserId());
        return this.updateById(entity);
    }

    @Override
    public void delete(MessageTemplateEntity entity) {
        this.removeById(entity.getId());
    }

    @Override
    public boolean isExistByFullName(String fullName, String id) {
        QueryWrapper<MessageTemplateEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(MessageTemplateEntity::getFullName, fullName);
        if (!StringUtil.isEmpty(id)) {
            queryWrapper.lambda().ne(MessageTemplateEntity::getId, id);
        }
        return this.count(queryWrapper) > 0 ? true : false;
    }

    @Override
    public boolean isExistByEnCode(String enCode, String id) {
        QueryWrapper<MessageTemplateEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(MessageTemplateEntity::getEnCode, enCode);
        if (!StringUtil.isEmpty(id)) {
            queryWrapper.lambda().ne(MessageTemplateEntity::getId, id);
        }
        return this.count(queryWrapper) > 0 ? true : false;
    }

}




