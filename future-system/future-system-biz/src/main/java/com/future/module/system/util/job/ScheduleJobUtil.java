package com.future.module.system.util.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.future.common.util.JsonUtil;
import com.future.common.util.StringUtil;
import com.future.module.system.model.schedule.ScheduleJobModel;
import com.future.reids.config.ConfigValueUtil;
import com.future.reids.util.RedisUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * 流程设计
 *
 * @author Future Platform Group
 * @version v4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2022/6/15 17:37
 */
@Component
@Slf4j
@DependsOn("threadPoolTaskExecutor")
public class ScheduleJobUtil {
    /**
     * 缓存key
     */
    public static final String WORKTIMEOUT_REDIS_KEY = "idgenerator_Schedule";

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ConfigValueUtil configValueUtil;

    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;

    ScheduleJobUtil(ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(2, threadPoolTaskExecutor.getThreadPoolExecutor().getThreadFactory());
    }

    /**
     * 将数据放入缓存
     *
     * @param
     * @return
     * @copyright 直方信息科技有限公司
     * @date 2022/6/2
     */
    public void insertRedis(List<ScheduleJobModel> scheduleJobList, RedisUtil redisUtil) {
        for (ScheduleJobModel jobModel : scheduleJobList) {
            String id = jobModel.getId();
            String objectToString = JsonUtil.getObjectToString(jobModel);
            redisUtil.insertHash(WORKTIMEOUT_REDIS_KEY, id, objectToString);
        }
    }

    /**
     * 定时器取用数据调用创建方法
     *
     * @param
     * @return
     * @copyright 直方信息科技有限公司
     * @date 2022/6/2
     */
    public List<ScheduleJobModel> getListRedis(RedisUtil redisUtil) {
        List<ScheduleJobModel> scheduleJobList = new ArrayList<>();
        if (redisUtil.exists(WORKTIMEOUT_REDIS_KEY)) {
            Map<String, Object> map = redisUtil.getMap(WORKTIMEOUT_REDIS_KEY);
            for (String object : map.keySet()) {
                if (map.get(object) instanceof String) {
                    ScheduleJobModel scheduleJobModel = JsonUtil.getJsonToBean(String.valueOf(map.get(object)), ScheduleJobModel.class);
                    if(StringUtil.isNotEmpty(scheduleJobModel.getId())) {
                        scheduleJobList.add(scheduleJobModel);
                    }else {
                        redisUtil.removeHash(WORKTIMEOUT_REDIS_KEY,object);
                    }
                }else {
                    redisUtil.removeHash(WORKTIMEOUT_REDIS_KEY,object);
                }
            }
        }
        return scheduleJobList;
    }


}
