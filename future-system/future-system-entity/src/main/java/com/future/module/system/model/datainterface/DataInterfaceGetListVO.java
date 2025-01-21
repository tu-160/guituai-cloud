package com.future.module.system.model.datainterface;


import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 工作流调用弹框时使用
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-12-24
 */
@Data
public class DataInterfaceGetListVO implements Serializable {
    @Schema(description ="主键Id")
    private String id;
    @Schema(description ="接口名称")
    private String fullName;
    @Schema(description ="接口类型")
    private String type;
//    @Schema(description ="类别")
//    private String requestMethod;
    @Schema(description ="编码")
    private String enCode;
    @Schema(description ="请求参数")
    private String parameterJson;
    @Schema(description ="字段JSON")
    private String fieldJson;
    @Schema(description ="后置接口")
    private Integer isPostPosition;
    @Schema(description ="是否真分页：0-不分页，1-分页")
    private Integer hasPage;
}
