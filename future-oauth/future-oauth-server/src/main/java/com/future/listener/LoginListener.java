package com.future.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.future.common.base.UserInfo;
import com.future.common.util.DateUtil;
import com.future.common.util.IpUtil;
import com.future.common.util.UserProvider;
import com.future.module.message.SentMessageApi;
import com.future.module.oauth.utils.LoginHolder;
import com.future.module.system.LogApi;
import com.future.permission.UserApi;
import com.future.permission.entity.UserEntity;
import com.future.permission.model.user.UserUpdateModel;

import cn.dev33.satoken.listener.SaTokenListenerForSimple;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import lombok.extern.slf4j.Slf4j;


/**
 *
 * @author Future Platform Group
 * @copyright 直方信息科技有限公司
 */
@Component
@Slf4j
public class LoginListener extends SaTokenListenerForSimple {

    @Autowired
    private UserProvider userProvider;
    @Autowired
    private SentMessageApi messageApi;
    @Autowired
    private LogApi logApi;
    @Autowired
    private UserApi userApi;

    @Override
    public void doLogin(String loginType, Object loginId, String tokenValue, SaLoginModel loginModel) {
        println("用户登录：{}, 设备：{}, TOKEN：{}", loginId, loginType, tokenValue);
        UserInfo userInfo = userProvider.get();
        //临时用户登录不记录
        if(!UserProvider.isTempUser(userInfo)) {

            UserEntity entity = LoginHolder.getUserEntity();
            entity.setLogErrorCount(0);
            entity.setUnlockTime(null);
            entity.setEnabledMark(1);
            entity.setPrevLogIp(IpUtil.getIpAddr());
            entity.setPrevLogTime(DateUtil.getNowDate());
            entity.setLastLogIp(IpUtil.getIpAddr());
            entity.setLastLogTime(DateUtil.getNowDate());
            entity.setLogSuccessCount(entity.getLogSuccessCount() != null ? entity.getLogSuccessCount() + 1 : 1);
            userApi.updateById(new UserUpdateModel(entity, userInfo.getTenantId()));
        }
    }

    @Override
    public void doLogout(String loginType, Object loginId, String tokenValue) {
        println("用户退出：{}, 设备：{}, TOKEN：{}", loginId, loginType, tokenValue);
    }

    @Override
    public void doKickout(String loginType, Object loginId, String tokenValue) {
        println("用户踢出：{}, 设备：{}, TOKEN：{}", loginId, loginType, tokenValue);
        messageApi.logoutWebsocketByToken(tokenValue, null);
        //删除用户信息缓存, 保留Token状态记录等待自动过期, 如果用户不在线下次打开浏览器会提示被踢下线
        StpUtil.getTokenSessionByToken(tokenValue).logout();
    }

    @Override
    public void doReplaced(String loginType, Object loginId, String tokenValue) {
        println("用户顶替：{}, 设备：{}, TOKEN：{}", loginId, loginType, tokenValue);
        messageApi.logoutWebsocketByToken(tokenValue, null);
        StpUtil.getTokenSessionByToken(tokenValue).logout();
    }

    /**
     * 打印指定字符串
     * @param str 字符串
     */
    public void println(String str, Object... params) {
        if(log.isDebugEnabled()) {
            log.debug(str, params);
        }
    }
}
