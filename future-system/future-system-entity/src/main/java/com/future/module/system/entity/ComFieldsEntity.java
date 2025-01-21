package com.future.module.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.future.common.base.entity.SuperExtendEntity;

import lombok.Data;

import java.io.Serializable;

/**
 *
 * 常用字段表
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @author Future Platform Group
* @date 2020-07-23 09:54
 */
@Data
@TableName("base_common_fields")
public class ComFieldsEntity extends SuperExtendEntity.SuperExtendDEEntity<String> implements Serializable {

    @TableField("f_field_name")
    private String fieldName;

    @TableField("f_field")
    private String field;

    @TableField("f_data_type")
    private String datatype;

    @TableField("f_data_length")
    private String datalength;

    @TableField("f_allow_null")
    private String allowNull;

}

