package com.future.module.system.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.future.common.base.entity.SuperEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 短息模板表
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021年12月8日17:40:37
 */
//@TableName("base_sms_template")
@Data
public class SmsTemplateEntity extends SuperEntity<String> implements Serializable {

    /**
     * 短信提供商
     */
    @TableField(value = "F_COMPANY")
    private Integer company;

    /**
     * 应用编号
     */
    @TableField(value = "F_APPID")
    private String appId;

    /**
     * 签名内容
     */
    @TableField(value = "F_SIGNCONTENT")
    private String signContent;

    /**
     * 模板编号
     */
    @TableField(value = "F_TEMPLATEID")
    private String templateId;

    /**
     * 模板名称
     */
    @TableField(value = "F_FULLNAME")
    private String fullName;

    /**
     * 模板参数JSON
     */
    @TableField(value = "F_TEMPLATEJSON")
    private String templateJson;

    /**
     * 有效标志
     */
    @TableField("F_ENABLEDMARK")
    private Integer enabledMark;

    /**
     * 编码
     */
    @TableField("F_ENCODE")
    private String enCode;

    /**
     * endpoint
     */
    @TableField("F_ENDPOINT")
    private String endpoint;

    /**
     * 地域参数
     */
    @TableField("F_REGION")
    private String region;

}
