package com.future.permission.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.future.common.base.entity.SuperExtendEntity;

import lombok.Data;

import java.util.Date;

/**
 * 模块列表权限
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date ：2022/3/15 9:20
 */
@Data
@TableName("base_columns_purview")
public class ColumnsPurviewEntity extends SuperExtendEntity.SuperExtendEnabledEntity<String> {

    /**
     * 列表字段数组
     */
    @TableField("f_field_list")
    private String fieldList;
    /**
     * 模块ID
     */
    @TableField("f_module_id")
    private String moduleId;

}
