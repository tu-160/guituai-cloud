package com.future.permission.model.position;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021/3/12 15:31
 */
@Data
public class PosOrgConditionModel extends PosOrgModel {

    private String organizeIdTree;

    private String organizeId;

    @Schema(description ="前端解析唯一标识")
    private String onlyId;

}
