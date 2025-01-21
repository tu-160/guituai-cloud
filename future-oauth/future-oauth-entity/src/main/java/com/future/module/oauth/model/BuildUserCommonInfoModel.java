package com.future.module.oauth.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import com.future.common.base.UserInfo;
import com.future.common.model.BaseSystemInfo;
import com.future.module.system.entity.SystemEntity;
import com.future.permission.entity.UserEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuildUserCommonInfoModel implements Serializable {

    private UserInfo userInfo;
    private SystemEntity mainSystemEntity;
    private SystemEntity workSystemEntity;
    private UserEntity userEntity;
    private BaseSystemInfo baseSystemInfo;
    private String systemId;

}
