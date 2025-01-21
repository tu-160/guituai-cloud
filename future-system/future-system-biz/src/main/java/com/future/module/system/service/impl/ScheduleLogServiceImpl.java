package com.future.module.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.future.base.service.SuperServiceImpl;
import com.future.common.base.UserInfo;
import com.future.common.util.RandomUtil;
import com.future.common.util.UserProvider;
import com.future.module.system.entity.ScheduleLogEntity;
import com.future.module.system.mapper.ScheduleLogMapper;
import com.future.module.system.service.ScheduleLogService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 日程
 *
 * @author Future Platform Group
 * @copyright 直方信息科技有限公司
 */
@Service
public class ScheduleLogServiceImpl extends SuperServiceImpl<ScheduleLogMapper, ScheduleLogEntity> implements ScheduleLogService {


    @Autowired
    private UserProvider userProvider;

    @Override
    public List<ScheduleLogEntity> getListAll(List<String> scheduleIdList) {
        List<ScheduleLogEntity> list = new ArrayList<>();
        QueryWrapper<ScheduleLogEntity> queryWrapper = new QueryWrapper<>();
        if(scheduleIdList.size()>0){
            queryWrapper.lambda().in(ScheduleLogEntity::getScheduleId,scheduleIdList);
            queryWrapper.lambda().orderByDesc(ScheduleLogEntity::getCreatorTime);
            list = this.list(queryWrapper);
        }
        return list;
    }

    @Override
    public ScheduleLogEntity getInfo(String id) {
        QueryWrapper<ScheduleLogEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ScheduleLogEntity::getId,id);
        return this.getOne(queryWrapper);
    }

    @Override
    public void create(ScheduleLogEntity entity) {
        UserInfo userInfo = userProvider.get();
        entity.setId(RandomUtil.uuId());
        entity.setCreatorTime(new Date());
        entity.setCreatorUserId(userInfo.getUserId());
        this.save(entity);
    }

    @Override
    public void delete(List<String> scheduleIdList, String operationType) {
        List<ScheduleLogEntity> listAll = getListAll(scheduleIdList);
        for (ScheduleLogEntity scheduleLogEntity : listAll) {
            scheduleLogEntity.setOperationType(operationType);
            create(scheduleLogEntity);
        }
    }

    @Override
    public boolean update(String id, ScheduleLogEntity entity) {
        entity.setId(id);
        boolean flag = this.updateById(entity);
        return flag;
    }


}
