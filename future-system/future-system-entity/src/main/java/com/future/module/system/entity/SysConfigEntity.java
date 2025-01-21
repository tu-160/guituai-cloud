package com.future.module.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.future.common.base.entity.SuperExtendEntity;

import lombok.Data;

import java.io.Serializable;

/**
 * 系统配置
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2019年9月27日 上午9:18
 */
@Data
@TableName("base_sys_config")
public class SysConfigEntity extends SuperExtendEntity<String> implements Serializable {

    /**
     * 名称
     */
    @TableField("F_FULL_NAME")
    private String fullName;

    /**
     * 键
     */
    @TableField("F_KEY")
    private String fkey;

    /**
     * 值
     */
    @TableField("F_VALUE")
    private String value;

    /**
     * 分类
     */
    @TableField("F_CATEGORY")
    private String category;

}
