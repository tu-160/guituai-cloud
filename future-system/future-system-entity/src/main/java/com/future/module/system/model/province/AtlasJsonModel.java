package com.future.module.system.model.province;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 流程设计
 *
 * @author Future Platform Group
 * @version v4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2023/2/1 9:48:02
 */
@Data
public class AtlasJsonModel {
    private String type;
    private List<AtlasFeaturesModel> features;
}

