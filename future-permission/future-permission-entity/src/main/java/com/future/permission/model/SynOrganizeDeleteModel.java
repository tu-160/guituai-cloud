package com.future.permission.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import com.future.permission.entity.UserEntity;

/**
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-06-05
 */
@Data
@NoArgsConstructor
public class SynOrganizeDeleteModel {
    private Boolean isBatch;
    private String organizeId;
    private String accessToken;
    public SynOrganizeDeleteModel(Boolean isBatch, String organizeId, String accessToken) {
        this.isBatch = isBatch;
        this.organizeId = organizeId;
        this.accessToken = accessToken;
    }
}
