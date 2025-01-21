package com.future.permission.model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-06-05
 */
@Data
@NoArgsConstructor
public class SysThirdDeleteModel {
    private Boolean isBatch;
    private String userId;
    private String accessToken;

    public SysThirdDeleteModel(Boolean isBatch, String userId, String accessToken) {
        this.isBatch = isBatch;
        this.userId = userId;
        this.accessToken = accessToken;
    }
}
