package com.future.permission.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import com.future.permission.entity.OrganizeEntity;

/**
 * 组织或部门同步到第三方
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-06-05
 */
@Data
@NoArgsConstructor
public class SynOrganizeModel {
    private Boolean isBatch;
    private OrganizeEntity deptEntity;
    private String accessToken;

    public SynOrganizeModel(Boolean isBatch, OrganizeEntity deptEntity, String accessToken) {
        this.isBatch = isBatch;
        this.deptEntity = deptEntity;
        this.accessToken = accessToken;
    }
}
