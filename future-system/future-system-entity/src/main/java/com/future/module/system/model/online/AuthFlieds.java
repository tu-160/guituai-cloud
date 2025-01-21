package com.future.module.system.model.online;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 *
 * @author Future Platform Group
 * @version v4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2022/4/6
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthFlieds {

	private String id;
	/**
	 * 字段名
	 */
	private String fullName;
	/**
	 * 是否启用
	 */
	private Boolean status;

	/**
	 * encode
	 */
	private String encode;

	/**
	 * 规则 （0.主表规则 1.副表规则 2.子表规则）
	 */
	private Integer rule;

	/**
	 * 控件类型
	 */
	private String futureKey;

	/**
	 * 数据权限条件
	 */
	private String AuthCondition;

	/**
	 * 表名
	 */
	private String bindTableName;

	/**
	 * 子表规则key
	 */
	private String childTableKey;
	
	
}
