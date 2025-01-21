package com.future.permission.model;

import com.future.permission.entity.UserEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 同步到企业微信model
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-06-04
 */
@Data
@NoArgsConstructor
public class SynThirdQyModel {
    private Boolean isBatch;
    private UserEntity userEntity;
    private String accessToken;

    public SynThirdQyModel(Boolean isBatch, UserEntity userEntity, String accessToken) {
        this.isBatch = isBatch;
        this.userEntity = userEntity;
        this.accessToken = accessToken;
    }
}
