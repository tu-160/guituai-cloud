package com.future.module.system.model.smstemplate;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 回显短信模板
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-12-09
 */
@Data
public class SmsTemplateVO implements Serializable {
    private String id;
    private String templateId;
    private Integer company;
    private String signContent;
    private Integer enabledMark;
    private String fullName;

    private String enCode;
    private String endpoint;
    private String region;
}
