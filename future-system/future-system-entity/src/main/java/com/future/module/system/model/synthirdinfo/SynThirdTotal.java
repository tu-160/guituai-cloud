package com.future.module.system.model.synthirdinfo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 同步统计信息模型
 *
 * @@version V4.0.0
 * @@copyright 直方信息科技有限公司
 * @@author Future Platform Group
 * @date 2021/5/10 11:21
 */
@Data
public class SynThirdTotal {
    /**
     * 同步类型
     */
    @Schema(description = "同步类型")
    private String synType;
    /**
     * 记录总数
     */
    @Schema(description = "记录总数")
    private Integer recordTotal;
    /**
     * 同步成功记录数
     */
    @Schema(description = "同步成功记录数")
    private Long synSuccessCount;
    /**
     * 同步失败记录数
     */
    @Schema(description = "同步失败记录数")
    private Long synFailCount;
    /**
     * 未同步记录数
     */
    @Schema(description = "未同步记录数")
    private Long unSynCount;
    /**
     * 最后同步时间
     */
    @Schema(description = "最后同步时间")
    private Date synDate;

}
