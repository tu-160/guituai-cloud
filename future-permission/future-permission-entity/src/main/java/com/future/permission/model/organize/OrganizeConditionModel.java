package com.future.permission.model.organize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.future.common.base.Page;

/**
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date ：2022/6/8 14:05
 */
@Data
public class OrganizeConditionModel extends Page implements Serializable {

    @Schema(description = "部门id集合")
    private List<String> departIds;

    private Map<String, String> orgIdNameMaps;
}
