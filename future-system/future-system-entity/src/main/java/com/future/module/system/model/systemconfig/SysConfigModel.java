package com.future.module.system.model.systemconfig;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SysConfigModel {
    @NotBlank(message = "必填")
    @Schema(description ="系统名称")
    private String sysName;
    @NotBlank(message = "必填")
    @Schema(description ="系统描述")
    private String sysDescription;
    @NotBlank(message = "必填")
    @Schema(description ="系统版本")
    private String sysVersion;
    @NotBlank(message = "必填")
    @Schema(description ="版权信息")
    private String copyright;
    @NotBlank(message = "必填")
    @Schema(description ="公司名称")
    private String companyName;
    @NotBlank(message = "必填")
    @Schema(description ="公司简称")
    private String companyCode;
    @NotBlank(message = "必填")
    @Schema(description ="公司地址")
    private String companyAddress;
    @NotBlank(message = "必填")
    @Schema(description ="公司法人")
    private String companyContacts;
    @NotBlank(message = "必填")
    @Schema(description ="公司电话")
    private String companyTelePhone;
    @NotBlank(message = "必填")
    @Schema(description ="公司邮箱")
    private String companyEmail;

    /**
     * 登录图标
     */
    @Schema(description ="登录图标")
    private String loginIcon;

    /**
     * 导航图标
     */
    @Schema(description ="导航图标")
    private String navigationIcon;
    /**
     * logo图标
     */
    @Schema(description ="logo图标")
    private String logoIcon;
    /**
     * App图标
     */
    @Schema(description ="App图标")
    private String appIcon;

    /**
     * 1--后登陆踢出先登录
     * 2--同时登陆
     */
    @NotBlank(message = "必填")
    @Schema(description ="单一登录方式")
    private Integer singleLogin;
    /**
     * 密码错误次数
     */
    @Schema(description ="密码错误次数")
    @NotNull(message = "必填")
    private Integer passwordErrorsNumber;
    /**
     * 错误策略  1--账号锁定  2--延时登录
     */
    @Schema(description ="错误策略")
    private Integer lockType;
    /**
     * 延时登录时间
     */
    @Schema(description ="延时登录时间")
    private Integer lockTime;
    /**
     * 是否开启验证码
     */
    @Schema(description ="是否开启验证码")
    private Integer enableVerificationCode;
    /**
     * 验证码位数
     */
    @Schema(description ="验证码位数")
    private Integer verificationCodeNumber;


    @NotBlank(message = "必填")
    @Schema(description ="超出登出")
    private String tokenTimeout;
    @NotBlank(message = "必填")
    @Schema(description ="是否开启上次登录提醒")
    private Integer lastLoginTimeSwitch;
    @NotBlank(message = "必填")
    @Schema(description ="是否开启白名单验证")
    private Integer whitelistSwitch;
    @NotBlank(message = "必填")
    @Schema(description ="白名单")
    private String whiteListIp;
    @NotBlank(message = "必填")
    @Schema(description ="POP3服务主机地址")
    private String emailPop3Host;
    @NotBlank(message = "必填")
    @Schema(description ="POP3服务端口")
    private String emailPop3Port;
    @NotBlank(message = "必填")
    @Schema(description ="SMTP服务主机地址")
    private String emailSmtpHost;
    @NotBlank(message = "必填")
    @Schema(description ="邮件显示名称")
    private String emailSmtpPort;
    @NotBlank(message = "必填")
    @Schema(description ="系统名称")
    private String emailSenderName;
    @NotBlank(message = "必填")
    @Schema(description ="邮箱账户")
    private String emailAccount;
    @NotBlank(message = "必填")
    @Schema(description ="邮箱密码")
    private String emailPassword;
    @NotBlank(message = "必填")
    @Schema(description ="是否开启SSL服务登录")
    private Integer emailSsl;


    @NotBlank(message = "必填")
    @Schema(description ="授权密钥")
    private String registerKey;
    private String lastLoginTime;
    private String pageSize;
    private String sysTheme;
    private String isLog;

    // 短信配置
    /**
     * 阿里
     */
    private String aliAccessKey;
    private String aliSecret;

    /**
     * 腾讯
     */
    private String tencentSecretId;
    private String tencentSecretKey;
    private String tencentAppId;
    private String tencentAppKey;
    // End 短信配置

    /**
     * 企业微信配置
     */
    /** 发消息 */
    private String qyhCorpId;
    private String qyhAgentId;
    private String qyhAgentSecret;
    /** 同步消息 */
    private String qyhCorpSecret;
    private Integer qyhIsSynOrg;
    private Integer qyhIsSynUser;

    /**
     * 钉钉 发消息
     */
    private String dingSynAppKey;
    private String dingSynAppSecret;
    private String dingAgentId;
    private Integer dingSynIsSynOrg;
    private Integer dingSynIsSynUser;

    /**
     * 审批链接时效性
     */
    private String linkTime;

    /**
     * 链接点击次数
     */
    private Integer isClick;

    /**
     * 链接失效次数
     */
    private Integer unClickNum;

    /**  密码策略 */
    /**
     * 密码定期更新开关
     */
    private Integer passwordIsUpdatedRegularly;

    /**
     * 更新周期
     */
    private Integer updateCycle;

    /**
     * 提前N天提醒更新
     */
    private Integer updateInAdvance;

    /**
     * 密码强度限制开关
     */
    private Integer passwordStrengthLimit;

    /**
     * 最小长度开关
     */
    private Integer passwordLengthMin;

    /**
     * 密码最小长度限制
     */
    private Integer passwordLengthMinNumber;

    /**
     * 是否包含数字
     */
    private Integer containsNumbers;

    /**
     * 是否包含小写字母
     */
    private Integer includeLowercaseLetters;

    /**
     * 是否包含大写字母
     */
    private Integer includeUppercaseLetters;

    /**
     * 是否包含字符
     */
    private Integer containsCharacters;

    /**
     * 是否禁用旧密码开关
     */
    private Integer disableOldPassword;

    /**
     * 禁用旧密码个数
     */
    private Integer disableTheNumberOfOldPasswords;

    /**
     * 初始密码强制修改开关
     */
    private Integer mandatoryModificationOfInitialPassword;

    @Schema(description ="窗口标题")
    private String title;

}
