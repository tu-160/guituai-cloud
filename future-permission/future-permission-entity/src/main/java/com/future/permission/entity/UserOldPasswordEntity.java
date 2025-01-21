package com.future.permission.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.future.common.base.entity.SuperExtendEntity;

import lombok.Data;

import java.util.Date;

/**
 * 用户信息
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2019年9月26日 上午9:18
 */
@Data
@TableName(value = "base_user_old_password")
public class UserOldPasswordEntity extends SuperExtendEntity<String> {

    /**
     * userid
     */
    @TableField("F_USER_ID")
    private String userId;

    /**
     * 账户
     */
    @TableField("F_ACCOUNT")
    private String account;

    /**
     * 旧密码
     */
    @TableField("F_OLD_PASSWORD")
    private String oldPassword;

    /**
     * 秘钥
     */
    @TableField("F_SECRETKEY")
    private String secretkey;

}
