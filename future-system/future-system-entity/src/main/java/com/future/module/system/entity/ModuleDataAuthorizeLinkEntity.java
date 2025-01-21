package com.future.module.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.future.common.base.entity.SuperExtendEntity;

import lombok.Data;

/**
 * @author Future Platform Group
 * @version v4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2022/6/7
 */
@Data
@TableName("base_module_link")
public class ModuleDataAuthorizeLinkEntity extends SuperExtendEntity<String> {

	/**
	 * 菜单主键
	 */
	@TableField("f_module_id")
	private String moduleId;

	/**
	 * 数据源连接
	 */
	@TableField("f_link_id")
	private String linkId;

	/**
	 * 连接表名
	 */
	@TableField("f_link_tables")
	private String linkTables;

	/**
	 * 权限类型（表单权限，数据权限，列表权限）
	 */
	@TableField("f_type")
	private Integer dataType;

}
