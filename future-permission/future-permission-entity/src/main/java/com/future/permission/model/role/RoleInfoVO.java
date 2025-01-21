package com.future.permission.model.role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.future.permission.model.permission.PermissionVoBase;

/**
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021/3/12 15:31
 */
@Data
public class RoleInfoVO extends PermissionVoBase {
    @Schema(description ="主键")
    private String id;
    @Schema(description ="名称")
    private String fullName;
    @Schema(description ="编码")
    private String enCode;
    @Schema(description ="组织id数组树")
    private List<LinkedList<String>> organizeIdsTree;
    @Schema(description ="全局标识")
    private Integer globalMark;
    @Schema(description ="类型")
    private String type;
    @Schema(description ="状态")
    private Integer enabledMark;
    @Schema(description ="备注")
    private String description;
    @Schema(description ="排序")
    private Long sortCode;


}
