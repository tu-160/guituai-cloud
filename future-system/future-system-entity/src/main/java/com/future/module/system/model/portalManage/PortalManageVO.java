package com.future.module.system.model.portalManage;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 类功能
 *
 * @author Future Platform Group
 * @version v4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2023-02-16
 */
@Data
public class PortalManageVO {

    @Schema(description = "主键_id")
    private String id;

    @Schema(description = "门户_id")
    private String portalId;

    @Schema(description = "分类")
    private String categoryId;

    @Schema(description = "分类")
    private String categoryName;

    @Schema(description = "平台")
    private String platform;

    @Schema(description = "说明")
    private String description;

    @Schema(description = "门户名称")
    private String fullName;

    @Schema(description = "默认首页")
    private Integer homePageMark;

    @Schema(description = "排序码")
    private Long sortCode;

    @Schema(description = "有效标志")
    private Integer enabledMark;

    @Schema(description = "创建用户")
    private String creatorUser;

    @Schema(description = "创建时间")
    private Date creatorTime;

    @Schema(description = "最后修改人")
    private String lastModifyUser;

    @Schema(description = "最后修改时间")
    private Date lastModifyTime;

    @Schema(description = "系统_id")
    private String systemId;

}
