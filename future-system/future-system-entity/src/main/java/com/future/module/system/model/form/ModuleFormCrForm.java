package com.future.module.system.model.form;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-09-14
 */
@Data
public class ModuleFormCrForm {

    @Schema(description ="编码")
    private String enCode;

    @Schema(description ="状态")
    private Integer enabledMark;

    @Schema(description ="名称")
    private String fullName;

    @Schema(description ="备注")
    private String description;

    @Schema(description ="菜单id")
    private String moduleId;

    @Schema(description ="排序码")
    private Long sortCode;
    @Schema(description ="规则")
    private Integer fieldRule;
    @Schema(description ="绑定表")
    private String bindTable;
    private String childTableKey;
}
