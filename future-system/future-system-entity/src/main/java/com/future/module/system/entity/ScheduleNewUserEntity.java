package com.future.module.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.future.common.base.entity.SuperExtendEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


/**
 * 日程安排
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2019年9月26日 上午9:18
 */
@Data
@TableName("base_schedule_user")
public class ScheduleNewUserEntity extends SuperExtendEntity.SuperExtendDEEntity<String> {
    /**
     * 日程id
     */
    @TableField("F_SCHEDULE_ID")
    private String scheduleId;

    /**
     * 用户id
     */
    @TableField("F_TO_USER_ID")
    private String toUserId;

    /**
     * 类型(1-系统添加 2-用户添加)
     */
    @TableField("F_TYPE")
    private Integer type;

}
