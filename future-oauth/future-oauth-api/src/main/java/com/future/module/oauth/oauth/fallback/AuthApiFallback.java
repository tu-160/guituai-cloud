package com.future.module.oauth.oauth.fallback;

import java.util.Map;

import com.future.common.base.ActionResult;
import com.future.common.base.UserInfo;
import com.future.common.exception.LoginException;
import com.future.module.oauth.model.LoginVO;
import com.future.module.oauth.oauth.AuthApi;

public class AuthApiFallback implements AuthApi {
    @Override
    public ActionResult<LoginVO> login(Map<String, String> parameters) throws LoginException {
        return ActionResult.fail("登录失败");
    }

    @Override
    public UserInfo getCurrentUser() {
        return null;
    }

    @Override
    public void kickoutByToken(String[] tokens, String userId, String tenantId) {

    }
}
