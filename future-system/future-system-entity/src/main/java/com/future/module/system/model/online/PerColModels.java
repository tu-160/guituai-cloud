package com.future.module.system.model.online;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

import com.future.module.system.entity.ModuleDataAuthorizeSchemeEntity;

/**
 *
 *
 * @author Future Platform Group
 * @version v4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2022/4/7
 */
@Data
public class PerColModels {
	/**
	 * 数据权限
	 */
	private List<AuthFlieds> dataPermission;

	/**
	 * 表单权限
	 */
	private List<AuthFlieds> formPermission;

	/**
	 * 列表权限
	 */
	private List<AuthFlieds> listPermission;

	/**
	 * 按钮权限
	 */
	private List<AuthFlieds> buttonPermission;

	/**
	 * 数据权限方案
	 */
	private List<ModuleDataAuthorizeSchemeEntity> dataPermissionScheme;

}
