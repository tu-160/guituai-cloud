package com.future.module.system.model.schedule;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author Future Platform Group
 * @copyright 直方信息科技有限公司
 */
@Data
public class ScheduleNewInfoVO extends ScheduleNewListVO {

    @Schema(description ="参与人")
    private List<String> toUserIds;
    @Schema(description ="附件")
    private String files;

}
