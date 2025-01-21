package com.future.permission.model.role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021/3/12 15:31
 */
@Data
public class RoleCrForm {
    @NotBlank(message = "必填")
    @Schema(description ="角色名称")
    private String fullName;
    @NotBlank(message = "必填")
    @Schema(description ="角色编号")
    private String enCode;
    @NotNull(message = "必填")
    @Schema(description ="组织id集合")
    private List<List<String>> organizeIdsTree;
    @Schema(description ="是否全局(1:是，0:否)")
    private Integer globalMark;
    @NotNull(message = "必填")
    @Schema(description ="状态")
    private int enabledMark;
    @Schema(description ="说明")
    private String description;
    @Schema(description ="排序")
    private long sortCode;
}
