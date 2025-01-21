package com.future.module.system.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.future.common.base.entity.SuperExtendEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 列表权限
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2019年9月27日 上午9:18
 */
@Data
@TableName("base_module_column")
public class ModuleColumnEntity extends SuperExtendEntity.SuperExtendDEEntity<String> implements Serializable {

    /**
     * 列表上级
     */
    @TableField("f_parent_id")
    private String parentId;

    /**
     * 列表名称
     */
    @TableField("f_full_name")
    private String fullName;

    /**
     * 列表编码
     */
    @TableField("f_en_code")
    private String enCode;

    /**
     * 绑定表格Id
     */
    @TableField("f_bind_table")
    private String bindTable;

    /**
     * 绑定表格描述
     */
    @TableField("f_bind_table_name")
    private String bindTableName;

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
     * 子表规则key
     */
    @TableField("f_child_table_key")
    private String childTableKey;

}
