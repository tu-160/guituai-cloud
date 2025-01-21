package com.future.module.oauth.granter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import com.future.common.base.ActionResult;
import com.future.common.base.UserInfo;
import com.future.common.consts.AuthConsts;
import com.future.common.consts.DeviceType;
import com.future.common.exception.LoginException;
import com.future.common.granter.AbstractTokenGranter;
import com.future.common.model.BaseSystemInfo;
import com.future.common.util.UserProvider;
import com.future.module.oauth.model.LoginVO;

import static com.future.module.oauth.granter.TempUserTokenGranter.GRANT_TYPE;

import java.util.Arrays;
import java.util.Map;


/**
 * 临时用户认证
 * @user N
 * @author Future Platform Group
 * @copyright 直方信息科技有限公司
 * @date 2022/9/16 21:25
 */
@Slf4j
@Component(GRANT_TYPE)
public class TempUserTokenGranter extends AbstractTokenGranter{

    public static final String GRANT_TYPE = "tempuser";
    public static final Integer ORDER = 4;
    private static final String URL_LOGIN = "";


    public TempUserTokenGranter(){
        super(URL_LOGIN);
    }


    /**
     *
     * @param loginParameters {userId, tenantId}
     * @return
     * @param <T>
     * @throws LoginException
     */
    @Override
    public ActionResult granter(Map<String, String> loginParameters) throws LoginException {
        String token = loginParameters.get("token");
        //验证是否由内部发起
        if(!UserProvider.isValidInnerToken(token)){
            throw new LoginException("不允许访问此登录接口");
        }
        String userId = loginParameters.get("userId");
        String tenantId = loginParameters.get("tenantId");
        boolean limited = Boolean.valueOf(loginParameters.get("limited"));
        String device = limited?DeviceType.TEMPUSERLIMITED.getDevice():DeviceType.TEMPUSER.getDevice();
        //尝试获取已经登录的用户信息
        UserInfo userInfo;
        if(limited){
            //只获取限制类型的TOKEN
            userInfo = UserProvider.getUser(userId, tenantId, Arrays.asList(DeviceType.TEMPUSERLIMITED.getDevice()), null);
        }else{
            //排除限制类型的TOKEN
            userInfo = UserProvider.getUser(userId, tenantId, null, Arrays.asList(DeviceType.TEMPUSERLIMITED.getDevice()));
        }
        if(userInfo.getUserId() != null){
            return ActionResult.success(getLoginVo(userInfo));
        }
        userInfo = UserProvider.getUser();
        userInfo.setUserAccount(tenantId);
        userInfo.setUserId(userId);
        //切换租户
        switchTenant(userInfo);
        //获取系统配置
        BaseSystemInfo baseSystemInfo = getSysconfig(userInfo);
        //先设置用户类型
        userInfo.setLoginDevice(device);
        //登录账号
        super.loginAccount(userInfo, baseSystemInfo);
        //返回登录信息
        LoginVO loginResult = getLoginVo(userInfo);
        return ActionResult.success(loginResult);
    }

    @Override
    public int getOrder() {
        return ORDER;
    }


    @Override
    protected void preLogin(UserInfo userInfo, BaseSystemInfo baseSystemInfo) throws LoginException {

    }

    @Override
    protected void login(UserInfo userInfo, BaseSystemInfo baseSystemInfo) throws LoginException {
        UserProvider.loginNoRequest(userInfo, this.getLoginModel(userInfo, baseSystemInfo));
    }


    @Override
    protected void loginSuccess(UserInfo userInfo, BaseSystemInfo baseSystemInfo) {
        super.loginSuccess(userInfo, baseSystemInfo);

    }

    @Override
    protected DeviceType getDeviceType() {
        return DeviceType.TEMPUSER;
    }

    protected LoginVO getLoginVo(UserInfo userInfo){
        LoginVO loginVO = new LoginVO();
        loginVO.setTheme(userInfo.getTheme());
        loginVO.setToken(userInfo.getToken());
        return loginVO;
    }

    @Override
    public ActionResult logout() {
        //非临时用户不注销
        UserInfo userInfo = UserProvider.getUser();
        if(UserProvider.isTempUser(userInfo)){
            UserProvider.logoutByToken(userInfo.getToken());
        }
        return ActionResult.success("注销成功");
    }

    @Override
    protected String getGrantType() {
        return GRANT_TYPE;
    }

    @Override
    protected String getUserDetailKey() {
        return AuthConsts.USERDETAIL_USER_ID;
    }
}
