package com.future.module.system.model.moduledataauthorizescheme;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021/3/12 15:31
 */
@Data
public class DataAuthorizeSchemeCrForm {
    @NotBlank(message = "方案名称不能为空")
    private String fullName;

    private Object conditionJson;

    private String conditionText;

    private String moduleId;

    @NotBlank(message = "方案编码不能为空")
    private String enCode;

    /**
     * 全部数据标识
     */
    private Integer allData;
    /**
     * 分组匹配逻辑
     */
    private String matchLogic;
}
