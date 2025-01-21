package com.future.permission.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.future.common.base.entity.SuperExtendEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 类功能
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2022/1/27
 */
@Data
public class PermissionEntityBase extends SuperExtendEntity.SuperExtendDEEntity<String> {

    /**
     * 名称
     */
    @TableField("f_full_name")
    private String fullName;

    /**
     * 编码
     */
    @TableField("f_en_code")
    private String enCode;

    /**
     * 扩展属性
     */
    @TableField("f_property_json")
    private String propertyJson;

}

