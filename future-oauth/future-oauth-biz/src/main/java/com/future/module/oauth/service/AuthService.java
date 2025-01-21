package com.future.module.oauth.service;

import java.util.Map;

import com.future.common.base.ActionResult;
import com.future.common.exception.LoginException;
import com.future.module.oauth.model.LoginVO;

public interface AuthService {
    ActionResult<LoginVO> login(Map<String, String> parameters) throws LoginException;

    ActionResult kickoutByToken(String... tokens);

    ActionResult kickoutByUserId(String userId, String tenantId);
}
