package com.future.permission.model.organizerelation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date ：2022/5/11 17:27
 */
@Data
public class AutoGetMajorOrgIdModel implements Serializable {
    private String userId;
    private List<String> orgIds;
    private String organizeId;
    private String systemId;

    public AutoGetMajorOrgIdModel() {
    }

    public AutoGetMajorOrgIdModel(String userId, List<String> orgIds, String organizeId, String systemId) {
        this.userId = userId;
        this.orgIds = orgIds;
        this.organizeId = organizeId;
        this.systemId = systemId;
    }
}
