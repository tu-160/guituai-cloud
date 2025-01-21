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
 * 按钮权限
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2019年9月27日 上午9:18
 */
@Data
@TableName("base_module_button")
public class ModuleButtonEntity extends SuperExtendEntity.SuperExtendDEEntity<String> implements Serializable {

    /**
     * 按钮上级
     */
    @TableField("f_parent_id")
    private String parentId;

    /**
     * 按钮名称
     */
    @TableField("f_full_name")
    private String fullName;

    /**
     * 按钮编码
     */
    @TableField("f_en_code")
    private String enCode;

    /**
     * 按钮图标
     */
    @TableField("f_icon")
    private String icon;

    /**
     * 请求地址
     */
    @TableField("f_url_address")
    private String urlAddress;

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

}
