package com.future.module.system.model.datainterface;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date ：2022/6/2 18:09
 */
@Data
public class DataInterfaceParamModel implements Serializable {

    private String tenantId;

    private String origin;

    private List<DataInterfaceModel> paramList;

}
