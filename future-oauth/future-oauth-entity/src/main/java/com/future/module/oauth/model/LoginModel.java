package com.future.module.oauth.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 登陆判断是否需要验证码
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-12-31
 */
@Data
public class LoginModel implements Serializable {

    /**
     * 是否开启验证码
     */
    @Schema(description = "是否开启验证码")
    private Integer enableVerificationCode;

    /**
     * 验证码位数
     */
    @Schema(description = "验证码位数")
    private Integer verificationCodeNumber;

    public Integer getEnableVerificationCode() {
        return enableVerificationCode;
    }

    public void setEnableVerificationCode(Integer enableVerificationCode) {
        this.enableVerificationCode = enableVerificationCode;
    }

    public Integer getVerificationCodeNumber() {
        return verificationCodeNumber;
    }

    public void setVerificationCodeNumber(Integer verificationCodeNumber) {
        this.verificationCodeNumber = verificationCodeNumber;
    }
}
