package com.future.module.system.service;

import java.util.List;

import com.future.base.service.SuperService;
import com.future.module.system.entity.ScheduleNewUserEntity;

/**
 * 日程
 *
 * @author Future Platform Group
 * @copyright 直方信息科技有限公司
 */
public interface ScheduleNewUserService extends SuperService<ScheduleNewUserEntity> {

    /**
     * 列表
     *
     * @return
     */
    List<ScheduleNewUserEntity> getList(String scheduleId,Integer type);

    /**
     * 列表
     *
     * @return
     */
    List<ScheduleNewUserEntity> getList();

    /**
     * 创建
     *
     * @param entity 实体
     */
    void create(ScheduleNewUserEntity entity);

    /**
     * 删除
     *
     */
    void deleteByScheduleId(List<String> scheduleIdList);

    /**
     * 删除
     *
     */
    void deleteByUserId(List<String> scheduleIdList);
}
