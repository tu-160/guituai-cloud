package com.future.module.oauth.granter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.future.common.base.ActionResult;
import com.future.common.base.UserInfo;
import com.future.common.consts.AuthConsts;
import com.future.common.exception.LoginException;
import com.future.common.granter.UserDetailsServiceBuilder;
import com.future.common.model.*;
import com.future.common.util.*;
import com.future.database.util.TenantDataSourceUtil;
import com.future.module.oauth.model.LoginForm;
import com.future.module.oauth.model.LoginVO;
import com.future.module.oauth.utils.LoginHolder;
import com.future.permission.entity.UserEntity;

import static com.future.module.oauth.granter.OfficialTokenGranter.GRANT_TYPE;

import java.util.Map;


/**
 * 官网专用短信认证
 *
 * @author Future Platform Group
 * @user N
 * @copyright 直方信息科技有限公司
 * @date 2022/9/17 22:13
 */
@Slf4j
@Component(GRANT_TYPE)
public class OfficialTokenGranter extends PasswordTokenGranter {

    public static final String GRANT_TYPE = "official";

    @Autowired
    private UserDetailsServiceBuilder userDetailsServiceBuilder;


    @Override
    public ActionResult granter(Map<String, String> loginParameters) throws LoginException {
        LoginForm loginForm = JsonUtil.getJsonToBean(loginParameters, LoginForm.class);
        //校验短信验证码
        TenantDataSourceUtil.checkOfficialSmsCode(loginForm.getAccount(), loginForm.getCode(), 1);

        UserInfo userInfo = UserProvider.getUser();
        //切换租户
        switchTenant(userInfo);
        //获取系统配置
        BaseSystemInfo baseSystemInfo = getSysconfig(userInfo);
        //预检信息
        preAuthenticate(loginForm, userInfo, baseSystemInfo);
        //登录账号
        super.loginAccount(userInfo, baseSystemInfo);
        //返回登录信息
        LoginVO loginResult = getLoginVo(userInfo);
        return ActionResult.success(loginResult);
    }

    /**
     * 可重写实现邮箱、短信、TOTP验证
     *
     * @param loginForm
     * @param sysConfigInfo
     * @throws LoginException
     */
    protected void preAuthenticate(LoginForm loginForm, UserInfo userInfo, BaseSystemInfo sysConfigInfo) throws LoginException {
        //验证密码
        UserEntity userEntity = userDetailsServiceBuilder.getUserDetailService(AuthConsts.USERDETAIL_ACCOUNT).loadUserEntity(userInfo);
        try {
            authenticateLock(userEntity, sysConfigInfo);
        } catch (Exception e) {
            authenticateFailure(userEntity, sysConfigInfo);
            throw e;
        }
        LoginHolder.setUserEntity(userEntity);
    }

    @Override
    protected String getGrantType() {
        return GRANT_TYPE;
    }
}
