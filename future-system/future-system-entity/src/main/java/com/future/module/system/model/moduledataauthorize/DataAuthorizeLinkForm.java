package com.future.module.system.model.moduledataauthorize;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 数据权限 连接表单
 *
 * @author Future Platform Group
 * @version v4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2022/6/7
 */
@Data
public class DataAuthorizeLinkForm {
	@Schema(description = "主键")
	private String id;
	@Schema(description = "菜单id")
	@NotBlank(message = "必填")
	private String moduleId;
	@Schema(description = "连接id")
	private String linkId;
	@Schema(description = "连接表")
	private String linkTables;
	@Schema(description = "数据类型")
	private Integer dataType;
}
