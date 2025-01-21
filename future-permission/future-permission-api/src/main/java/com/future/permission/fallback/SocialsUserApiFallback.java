package com.future.permission.fallback;

import com.alibaba.fastjson.JSONObject;
import com.future.common.exception.LoginException;
import com.future.permission.SocialsUserApi;
import com.future.permission.entity.SocialsUserEntity;
import com.future.permission.model.socails.SocialsUserInfo;
import com.future.permission.model.socails.SocialsUserVo;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SocialsUserApiFallback implements SocialsUserApi {

    @Override
    public List<SocialsUserVo> getLoginList(String ticket) {
        return new ArrayList<>();
    }

    @Override
    public JSONObject binding(String source, String userId, String tenantId, String code, String state ){
        return new JSONObject();
    }

    @Override
    public SocialsUserInfo getSocialsUserInfo(String source, String code, String state) {
        return new SocialsUserInfo();
    }

    @Override
    public SocialsUserInfo getUserInfo(String source, String uuid, String socialName) throws LoginException {
        return null;
    }

    @Override
    public void loginAutoBinding(String socialType, String socialUnionid, String socialName, String userId, String tenantId) {

    }

    @Override
    public SocialsUserEntity getInfoBySocialId(String socialId, String socialType) {
        return null;
    }
}
