package com.future.permission.model.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

import com.future.common.base.user.UserTenantModel;
import com.future.permission.entity.UserEntity;

/**
 * 修改用户模型
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date ：2022/5/13 14:02
 */
@Data
public class UserUpdateModel extends UserTenantModel implements Serializable {
    private UserEntity entity;

    public UserUpdateModel(UserEntity entity, String tenantId) {
        super(tenantId);
        this.entity = entity;
    }

    public UserUpdateModel() {
    }
}
