package com.future.module.system.model.smstemplate;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Map;

/**
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-12-09
 */
@Data
public class SmsTemplateCrForm implements Serializable {
    @NotBlank(message = "模板编号不能为空")
    private String templateId;
    @NotBlank(message = "模板名称不能为空")
    private String fullName;
    @NotNull(message = "短信厂家不能为空")
    private Integer company;
    private String appId;
    @NotBlank(message = "签名内容不能为空")
    private String signContent;
    private Integer enabledMark;
    /**
     * 测试短信接收人
     */
    private String phoneNumbers;

    private Map<String, Object> parameters;

    @NotBlank(message = "模板编码不能为空")
    private String enCode;

    /**
     * Endpoint
     */
    private String endpoint;

    /**
     * region
     */
    private String region;
}
