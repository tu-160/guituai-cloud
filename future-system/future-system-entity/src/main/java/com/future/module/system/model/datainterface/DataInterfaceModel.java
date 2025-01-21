package com.future.module.system.model.datainterface;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 自定义参数模型
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-12-13
 */
@Data
public class DataInterfaceModel extends ParamModel implements Serializable {

    /**
     * 是否为空（0允许，1不允许）
     */
    @Schema(description = "是否为空（0允许，1不允许）")
    private Integer required;

}
