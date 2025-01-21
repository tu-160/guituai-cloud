package com.future.module.system.model.online;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 可视化菜单对象
 *
 * @author Future Platform Group
 * @version v4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2022/4/6
 */
@Data
public class VisualMenuModel {
	/**
	 * 功能id
	 */
	private String id;

	/**
	 * pc 按钮配置
	 */
	private PerColModels pcPerCols;

	/**
	 * app 按钮配置
	 */
	private PerColModels appPerCols;

	/**
	 * 功能名
	 */
	private String fullName;

	/**
	 * 功能编码
	 */
	private String encode;

	private Integer pc;

	private Integer app;

	private String pcModuleParentId;

	private String appModuleParentId;

	private String pcSystemId;

	private String appSystemId;

	private Integer type;
}
