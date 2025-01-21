package com.future.module.system.model.online;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-12-15
 */
@Data
public class BatchOnlineModel implements Serializable {
    @Schema(description = "id集合")
    private String[] ids;
}
