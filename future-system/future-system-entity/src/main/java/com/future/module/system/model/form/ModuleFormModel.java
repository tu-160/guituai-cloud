package com.future.module.system.model.form;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-09-14
 */
@Data
public class ModuleFormModel {
    private String id;
    private String fullName;
    private String parentId;
    private String enCode;
    private String moduleId;
    private String icon;
    private String systemId;
    private Long sortCode=999999L;
    private Long creatorTime;
    private Date creatorTimes;

    public Long getCreatorTime() {
        if (this.creatorTimes != null && this.creatorTime == null) {
            return this.getCreatorTimes().getTime();
        } else if (this.creatorTime != null){
            return this.creatorTime;
        }
        return 0L;
    }
}
