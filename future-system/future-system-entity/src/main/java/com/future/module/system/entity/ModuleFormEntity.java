package com.future.module.system.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.future.common.base.entity.SuperExtendEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 *
 * 表单权限
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @author Future Platform Group
* @date 2021-09-14
 */
@Data
@TableName("base_module_form")
public class ModuleFormEntity extends SuperExtendEntity.SuperExtendDEEntity<String> {

    /**
     * 表单上级
     */
    @TableField("f_parent_id")
    private String parentId;

    /**
     * 表单名称
     */
    @TableField("f_full_name")
    private String fullName;

    /**
     * 表单编码
     */
    @TableField("f_en_code")
    private String enCode;

    /**
     * 扩展属性
     */
    @TableField("f_property_json")
    private String propertyJson;

    /**
     * 功能主键
     */
    @TableField("f_module_id")
    private String moduleId;

    /**
     * 字段规则 主从
     */
    @TableField("f_field_rule")
    private Integer fieldRule;

    /**
     * 绑定表格Id
     */
    @TableField("f_bind_table")
    private String bindTable;

    /**
     * 子表规则key
     */
    @TableField("f_child_table_key")
    private String childTableKey;

}
