package com.future.module.system.model.base;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date ：2022/6/22 14:26
 */
@Data
public class SystemBaeModel extends SystemCrModel implements Serializable {

    private String id;
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
