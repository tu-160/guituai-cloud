package com.future.permission.model.user;

import com.future.permission.entity.UserEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-11-23
 */
@Data
public class UserUpModel {

    private Integer num;

    private UserEntity entity;

    public UserUpModel() {
    }

    public UserUpModel(Integer num, UserEntity entity) {
        this.num = num;
        this.entity = entity;
    }
}
