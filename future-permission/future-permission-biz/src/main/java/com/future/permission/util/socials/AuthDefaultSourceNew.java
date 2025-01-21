package com.future.permission.util.socials;

import me.zhyd.oauth.config.AuthSource;
import me.zhyd.oauth.request.AuthDefaultRequest;
import me.zhyd.oauth.request.AuthDingTalkRequest;

/**
 * 流程设计
 *
 * @author Future Platform Group
 * @version v4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2022/7/15 17:20:40
 */
public enum AuthDefaultSourceNew implements AuthSource {
    DINGTALK_NEW {
        public String authorize() {
            return "https://login.dingtalk.com/oauth2/auth";
        }

        public String accessToken() {
            return "https://api.dingtalk.com/v1.0/oauth2/userAccessToken";

        }
        public String userInfo() {
            return "https://api.dingtalk.com/v1.0/contact/users/me";
        }
        public Class<? extends AuthDefaultRequest> getTargetClass() {
            return AuthDingTalkNewRequest.class;
        }
    },
    WECHAT_APPLETS {
        @Override
        public String authorize() {
            return null;
        }

        @Override
        public String accessToken() {
            return null;
        }

        public String userInfo() {
            return "https://api.weixin.qq.com/sns/jscode2session";
        }
        public Class<? extends AuthDefaultRequest> getTargetClass() {
            return AuthWechatAppletsRequest.class;
        }
    },;

    private AuthDefaultSourceNew() {
    }
}
