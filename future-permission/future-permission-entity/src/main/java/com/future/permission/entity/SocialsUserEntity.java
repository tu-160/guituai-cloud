package com.future.permission.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.future.common.base.entity.SuperExtendEntity;

import lombok.Data;

import java.util.Date;

/**
 * 流程设计
 *
 * @author Future Platform Group
 * @version v4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2022/7/14 9:28:32
 */
@Data
@TableName("base_socials_users")
public class SocialsUserEntity extends SuperExtendEntity<String> {

    /**
     * 用户id
     */
    @TableField("F_USER_ID")
    private String userId;

    /**
     * 第三方类型
     */
    @TableField("F_SOCIAL_TYPE")
    private String socialType;

    /**
     * 第三方账号id
     */
    @TableField("F_SOCIAL_ID")
    private String socialId;

    /**
     * 第三方账号
     */
    @TableField("F_SOCIAL_NAME")
    private String socialName;

}
