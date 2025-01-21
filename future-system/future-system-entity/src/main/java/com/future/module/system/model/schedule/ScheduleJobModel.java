package com.future.module.system.model.schedule;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.future.common.base.UserInfo;

@Data
public class ScheduleJobModel {
    private UserInfo userInfo = new UserInfo();
    private Date scheduleTime = new Date();
    private List<String> userList = new ArrayList<>();
    private String id;
}
