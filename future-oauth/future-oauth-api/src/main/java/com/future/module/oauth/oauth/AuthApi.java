package com.future.module.oauth.oauth;

import com.future.common.base.UserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import com.future.common.base.ActionResult;
import com.future.common.exception.LoginException;
import com.future.feign.utils.FeignName;
import com.future.module.oauth.model.LoginVO;

import java.util.Map;

@FeignClient(name = FeignName.OAUTH_SERVER_NAME)
public interface AuthApi {

    /**
     * 只能使用登录临时用户{userId, tenantId}
     * @param parameters
     * @return
     * @throws LoginException
     */
    @RequestMapping(value = "/Login", method = {RequestMethod.POST})
    ActionResult<LoginVO> login(@RequestParam Map<String, String> parameters) throws LoginException;

    @GetMapping("/getCurrentUser")
    public UserInfo getCurrentUser();

    @PostMapping(value = {"/KickoutToken" })
    void kickoutByToken(@RequestParam(value = "tokens", required = false) String[] tokens, @RequestParam(name = "userId", required = false) String userId, @RequestParam(name = "tenantId", required = false) String tenantId);

}
