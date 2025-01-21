package com.future.module.system.model.commonword;



import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * 审批常用语 Entity
 *
 * @author Future Platform Group
 * @version v4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2023-01-06
 */
@Data
@Schema(description = "CommonWords对象")
public class CommonWordsVO implements Serializable {

    @Schema(description = "自然主键")
    private String id;
    @Schema(description = "应用名称")
    private String systemNames;
    @Schema(description = "常用语")
    private String commonWordsText;
    @Schema(description = "常用语类型(0:系统,1:个人)")
    private Integer commonWordsType;
    @Schema(description = "排序")
    private Long sortCode;
    @Schema(description = "有效标志")
    private Integer enabledMark;

}
