package com.future.permission.model.position;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021/3/12 15:31
 */
@Data
public class PositionListVO {
    @Schema(description = "主键")
    private String id;
    @Schema(description = "名称")
    private String fullName;
    @Schema(description = "编码")
    private String enCode;
    @Schema(description = "分类")
    private String type;
    @Schema(description = "创建时间")
    private Long creatorTime;
    @Schema(description = "说明")
    private String description;
    @Schema(description = "部门")
    private String department;
    @Schema(description = "有效标志")
    private Integer enabledMark;
    @Schema(description ="排序")
    private Long sortCode;
    @Schema(description = "组织id")
    private String organizeId;
}
