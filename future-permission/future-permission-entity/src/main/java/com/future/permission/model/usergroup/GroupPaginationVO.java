package com.future.permission.model.usergroup;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户分组管理列表返回
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date ：2022/3/11 8:58
 */
@Data
public class GroupPaginationVO implements Serializable {
    @Schema(description = "主键")
    private String id;
    @Schema(description = "名称")
    private String fullName;
    @Schema(description = "编码")
    private String enCode;
    @Schema(description = "类型")
    private String type;
    @Schema(description = "说明")
    private String description;
    @Schema(description = "排序码")
    private Long sortCode;
    @Schema(description = "创建时间")
    private Long creatorTime;
    @Schema(description = "状态")
    private Integer enabledMark;
}
