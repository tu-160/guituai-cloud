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
 * 数据权限方案
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2019年9月27日 上午9:18
 */
@Data
@TableName("base_module_scheme")
public class ModuleDataAuthorizeSchemeEntity extends SuperExtendEntity.SuperExtendDEEntity<String> implements Serializable {

    /**
     * 方案编码
     */
    @TableField("f_en_code")
    private String enCode;

    /**
     * 方案名称
     */
    @TableField("f_full_name")
    private String fullName;

    /**
     * 条件规则Json
     */
    @TableField("f_condition_json")
    private String conditionJson;

    /**
     * 条件规则描述
     */
    @TableField("f_condition_text")
    private String conditionText;

    /**
     * 功能主键
     */
    @TableField("f_module_id")
    private String moduleId;

    /**
     * 全部数据标识
     */
    @TableField("f_all_data")
    private Integer allData;

    /**
     * 分组匹配逻辑
     */
    @TableField("f_match_logic")
    private String matchLogic;

}
