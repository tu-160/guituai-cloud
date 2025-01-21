package com.future.module.system.model.schedule;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Future Platform Group
 * @copyright 直方信息科技有限公司
 */
@Data
public class ScheduleNewCrForm {

    private String category;

    private String urgent = "1";

    private String title;

    private String content;

    private Integer allDay = 1;

    private Long startDay = System.currentTimeMillis();

    private String startTime = "00:00";

    private Long endDay = System.currentTimeMillis();

    private String endTime = "23:59";

    private Integer duration = -1;

    private List<String> toUserIds = new ArrayList<>();

    private String color;

    private Integer reminderTime = -2;

    private Integer reminderType = 1;

    private String send;

    private String sendName;

    private Integer repetition = 1;

    private Long repeatTime;
    @Schema(description ="附件")
    private String files;
}
