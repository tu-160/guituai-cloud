package com.future.module.system.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.future.common.base.entity.SuperEntity;
import com.future.common.base.entity.SuperExtendEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统功能
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2019年9月27日 上午9:18
 */
@Data
@TableName("base_module")
public class ModuleEntity extends SuperExtendEntity.SuperExtendDEEntity<String> implements Serializable {

    /**
     * 功能上级
     */
    @TableField("f_parent_id")
    private String parentId="0";

    /**
     * 功能类别
     */
    @TableField("f_type")
    private Integer type;

    /**
     * 功能名称
     */
    @TableField("f_full_name")
    private String fullName;

    /**
     * 功能编码
     */
    @TableField("f_en_code")
    private String enCode;

    /**
     * 功能地址
     */
    @TableField("f_url_address")
    private String urlAddress;

    /**
     * 按钮权限
     */
    @TableField("f_is_button_authorize")
    private Integer isButtonAuthorize;

    /**
     * 列表权限
     */
    @TableField("f_is_column_authorize")
    private Integer isColumnAuthorize;

    /**
     * 数据权限
     */
    @TableField("f_is_data_authorize")
    private Integer isDataAuthorize;

    /**
     * 表单权限
     */
    @TableField("f_is_form_authorize")
    private Integer isFormAuthorize;

    /**
     * 扩展属性
     */
    @TableField("f_property_json")
    private String propertyJson;

    /**
     * 菜单图标
     */
    @TableField("f_icon")
    private String icon;
    /**
     * 链接目标
     */
    @TableField("f_link_target")
    private String linkTarget;
    /**
     * 菜单分类 Web、App
     */
    @TableField("f_category")
    private String category;
    /**
     * 关联功能id
     */
    @TableField("f_module_id")
    private String moduleId;

    /**
     * 关联系统id
     */
    @TableField("f_system_id")
    private String systemId;

}
