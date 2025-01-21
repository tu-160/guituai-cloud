package com.future.module.system.model.commonword;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 类功能
 *
 * @author Future Platform Group
 * @version v4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2023-01-07
 */
@Data
@Schema(description = "CommonWordsForm对象")
public class CommonWordsForm {

    @Schema(description = "常用语Id")
    private String id;
    @Schema(description = "常用语类型(0:系统,1:个人)")
    private Integer commonWordsType;
    @Schema(description = "常用语")
    private String commonWordsText;
    @Schema(description = "排序")
    private Long sortCode;
    @Schema(description = "有效标志")
    private Integer enabledMark;

}
