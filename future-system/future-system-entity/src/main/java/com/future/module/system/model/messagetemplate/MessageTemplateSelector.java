package com.future.module.system.model.messagetemplate;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-12-11
 */
@Data
public class MessageTemplateSelector implements Serializable {
    @Schema(description = "主键")
    private String id;
    @Schema(description = "模板名称")
    private String fullName;
    @Schema(description = "消息类型")
    private String category;
    @Schema(description = "标题")
    private String title;
    @Schema(description = "内容")
    private String content;
    @Schema(description = "模板参数JSON")
    private String templateJson;
    @Schema(description = "编码")
    private String enCode;
}
