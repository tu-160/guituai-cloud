package com.future.module.system.model.province;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2022-01-05
 */
@Data
public class AreaModel implements Serializable {
    // id集合
    private List<List<String>> idsList;
}
