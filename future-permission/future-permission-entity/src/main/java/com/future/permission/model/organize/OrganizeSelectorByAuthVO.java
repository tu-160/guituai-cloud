package com.future.permission.model.organize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 组织树模型
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date ：2022/6/28 9:10
 */
@Data
public class OrganizeSelectorByAuthVO extends OrganizeSelectorVO implements Serializable {

    @Schema(description = "是否可选")
    private Boolean disabled = false;

}
