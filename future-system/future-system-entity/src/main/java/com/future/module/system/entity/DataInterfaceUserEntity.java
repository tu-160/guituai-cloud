package com.future.module.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.future.common.base.entity.SuperBaseEntity;

import lombok.Data;

/**
 * @author Future Platform Group
 * @version v4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021/9/20 9:22
 */
@Data
@TableName("base_data_interface_user")
public class DataInterfaceUserEntity extends SuperBaseEntity.SuperCBaseEntity<String> {
    /**
     * 用户主键
     */
    @TableField("f_user_id")
    private String userId;
    /**
     * 用户密钥
     */
    @TableField("f_user_key")
    private String userKey;
    /**
     * 接口认证主键
     */
    @TableField("f_oauth_id")
    private String oauthId;
    /**
     * 排序
     */
    @TableField("f_sort_code")
    private Long sortCode;
}
