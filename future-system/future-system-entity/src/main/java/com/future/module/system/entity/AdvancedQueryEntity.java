package com.future.module.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.future.common.base.entity.SuperExtendEntity;

import lombok.Data;

import java.util.Date;

/**
 *  高级查询
 *
 * @author Future Platform Group
 * @version v4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2022/5/30
 */
@Data
@TableName("base_advanced_query_scheme")
public class AdvancedQueryEntity extends SuperExtendEntity<String> {

	/**
	 * 方案名称
	 */
	@TableField("f_full_name")
	private String fullName;

	/**
	 * 方案名称
	 */
	@TableField("f_match_logic")
	private String matchLogic;

	/**
	 * 条件规则Json
	 */
	@TableField("f_condition_json")
	private String conditionJson;

	/**
	 * 菜单主键
	 */
	@TableField("f_module_id")
	private String moduleId;

}
