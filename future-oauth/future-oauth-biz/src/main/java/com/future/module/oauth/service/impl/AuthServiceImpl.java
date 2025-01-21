package com.future.module.oauth.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.future.common.base.ActionResult;
import com.future.common.base.UserInfo;
import com.future.common.exception.LoginException;
import com.future.common.granter.TokenGranter;
import com.future.common.granter.TokenGranterBuilder;
import com.future.common.util.StringUtil;
import com.future.common.util.TenantProvider;
import com.future.common.util.UserProvider;
import com.future.module.oauth.model.LoginVO;
import com.future.module.oauth.service.AuthService;
import com.future.module.oauth.utils.LoginHolder;
import com.future.module.system.LogApi;
import com.future.module.system.model.logmodel.WriteLogModel;

import java.util.Map;

/**
 * 登录与退出服务 其他服务调用
 */
@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Autowired
    private TokenGranterBuilder tokenGranterBuilder;
    @Autowired
    private LogApi logApi;

    /**
     * 登录
     * @param parameters {grant_type}
     * @return
     * @throws LoginException
     */
    public ActionResult<LoginVO> login(Map<String, String> parameters) throws LoginException{
        long millis = System.currentTimeMillis();
        TokenGranter tokenGranter = tokenGranterBuilder.getGranter(parameters.getOrDefault("grant_type", ""));
        ActionResult<LoginVO> result;
        UserInfo userInfo = new UserInfo();
        try {
            String account = parameters.get("account");
            userInfo.setUserAccount(account);
            UserProvider.setLocalLoginUser(userInfo);
            result = tokenGranter.granter(parameters);
            //写入日志
            if (StringUtil.isEmpty(parameters.get("userId"))) {
                logApi.writeLogAsync(WriteLogModel.builder()
                        .userId(userInfo.getUserId())
                        .userInfo(userInfo)
                        .userName(userInfo.getUserName() + "/" + userInfo.getUserAccount())
                        .abstracts("登录成功")
                        .loginMark(1)
                        .requestDuration(System.currentTimeMillis() - millis).build());
            }
        }catch (Exception e){
            if(!(e instanceof LoginException)){
                String msg = e.getMessage();
                if(msg == null){
                    msg = "登录异常";
                }
                log.error("登录异常 {}", e.getMessage(), e);
                throw new LoginException(msg);
            }
            String userName = StringUtil.isNotEmpty(userInfo.getUserName()) ? userInfo.getUserName()+"/"+userInfo.getUserAccount() : userInfo.getUserAccount();
            logApi.writeLogAsync(WriteLogModel.builder()
                    .userId(userInfo.getUserId())
                    .userName(userName)
                    .abstracts(e.getMessage())
                    .userInfo(userInfo)
                    .loginMark(0)
                    .requestDuration(System.currentTimeMillis()-millis).build());
            throw e;
        }finally{
            LoginHolder.clearUserEntity();
            TenantProvider.clearBaseSystemIfo();
        }
        return result;
    }


    /**
     * 踢出用户, 用户将收到Websocket下线通知
     * 执行流程：认证服务退出用户->用户踢出监听->消息服务发送Websocket推送退出消息
     * @param tokens
     */
    public ActionResult kickoutByToken(String... tokens){
        UserProvider.kickoutByToken(tokens);
        return ActionResult.success();
    }

    /**
     * 踢出用户, 用户将收到Websocket下线通知
     * 执行流程：认证服务退出用户->用户踢出监听->消息服务发送Websocket推送退出消息
     * @param userId
     * @param tenantId
     */
    public ActionResult kickoutByUserId(String userId, String tenantId){
        UserProvider.kickoutByUserId(userId, tenantId);
        return ActionResult.success();
    }
}
