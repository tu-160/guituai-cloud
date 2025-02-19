package com.future.module.system.model.datainterface;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 数据接口调用日志
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-06-10
 */
@Data
public class DataInterfaceLogVO implements Serializable {

    @Schema(description = "主键")
    private String id;

    @Schema(description = "调用时间")
    private Date invokTime;

    @Schema(description = "用户id")
    private String userId;

    @Schema(description = "调用ip")
    private String invokIp;

    @Schema(description = "调用设备")
    private String invokDevice;

    @Schema(description = "调用类型")
    private String invokType;

    @Schema(description = "调用响应时间")
    private Integer invokWasteTime;

}
