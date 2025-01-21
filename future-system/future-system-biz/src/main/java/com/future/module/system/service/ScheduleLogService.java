package com.future.module.system.service;

import java.util.List;

import com.future.base.service.SuperService;
import com.future.module.system.entity.ScheduleLogEntity;

/**
 * 日程
 *
 * @author Future Platform Group
 * @copyright 直方信息科技有限公司
 */
public interface ScheduleLogService extends SuperService<ScheduleLogEntity> {

    /**
     * 列表
     *
     * @return
     */
    List<ScheduleLogEntity> getListAll(List<String> scheduleIdList);

    /**
     * 信息
     *
     * @param id 主键值
     * @return 单据规则
     */
    ScheduleLogEntity getInfo(String id);

    /**
     * 创建
     *
     * @param entity 实体
     */
    void create(ScheduleLogEntity entity);

    /**
     * 删除
     * @param scheduleIdList
     */
    void delete(List<String> scheduleIdList,String operationType);
    /**
     * 更新
     *
     * @param id     主键值
     * @param entity 实体对象
     * @return ignore
     */
    boolean update(String id, ScheduleLogEntity entity);

}
