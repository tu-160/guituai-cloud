package com.future.module.system.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.future.base.service.SuperServiceImpl;
import com.future.module.system.entity.PrintLogEntity;
import com.future.module.system.mapper.PrintLogMapper;
import com.future.module.system.model.printlog.PrintLogQuery;
import com.future.module.system.service.PrintLogService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PrintLogServiceImpl extends SuperServiceImpl<PrintLogMapper, PrintLogEntity> implements PrintLogService {

    @Override
    public List<PrintLogEntity> getListId(String printId, PrintLogQuery page) {
        PageHelper.startPage((int) page.getCurrentPage(), (int) page.getPageSize(), false);
        List<String> listId = this.getBaseMapper().getListId(printId, page.getKeyword());
        PageInfo pageInfo = new PageInfo(listId);
        if (pageInfo.getList().size() > 0) {
            QueryWrapper<PrintLogEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(PrintLogEntity::getId, pageInfo.getList());
            Long startTime = page.getStartTime();
            Long endTime = page.getEndTime();
            if (!ObjectUtil.isEmpty(startTime) && !ObjectUtil.isEmpty(endTime)) {
                queryWrapper.lambda().between(PrintLogEntity::getCreatorTime, new Date(startTime), new Date(endTime));
            }
            queryWrapper.lambda().orderByDesc(PrintLogEntity::getCreatorTime);
            page.setTotal(pageInfo.getList().size());
            return this.list(queryWrapper);
        }
        return new ArrayList<>();
    }
}