package com.future.module.system.model.smstemplate;
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
public class SmsTemplateSelector implements Serializable {
    private String id;
    private String fullName;
    private String enCode;
    private String company;
}
