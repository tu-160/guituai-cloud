package com.future.module.system.model.province;

import com.alibaba.fastjson.annotation.JSONField;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 流程设计
 *
 * @author Future Platform Group
 * @version v4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2023/3/17 14:09:11
 */
@Schema(description="省市区下拉参数模型")
@Data
public class ProvinceSelectModel {
    @Schema(description = "父级id")
    @NotBlank(message = "必填")
    private String pid;
    @Schema(description = "选中id列表/查询子集时不传值")
    private List<List<String>> ids;
}
