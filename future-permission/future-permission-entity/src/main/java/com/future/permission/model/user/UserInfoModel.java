package com.future.permission.model.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

import com.future.common.base.user.UserTenantModel;

/**
 * 用户信息模型
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date ：2022/5/10 16:17
 */
@Data
public class UserInfoModel extends UserTenantModel implements Serializable {
    private String userId;

    public UserInfoModel() {
    }

    public UserInfoModel(String userId, String tenantId) {
        super(tenantId);
        this.userId = userId;
    }
}
