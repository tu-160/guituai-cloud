package com.future.util;

import cn.dev33.satoken.context.model.SaRequest;
import org.springframework.http.server.reactive.ServerHttpRequest;

import com.future.common.util.StringUtil;

public class ReactorUtil {

    public static String getIpAddr(SaRequest serverHttpRequest) {
        String xIp = serverHttpRequest.getHeader("X-Real-IP");
        String xFor = serverHttpRequest.getHeader("X-Forwarded-For");
        if (StringUtil.isNotEmpty(xFor) && !"unKnown".equalsIgnoreCase(xFor)) {
            int index = xFor.indexOf(",");
            if (index != -1) {
                return xFor.substring(0, index);
            } else {
                return xFor;
            }
        }
        xFor = xIp;
        if (StringUtil.isNotEmpty(xFor) && !"unKnown".equalsIgnoreCase(xFor)) {
            return xFor;
        }
        if (StringUtil.isBlank(xFor) || "unknown".equalsIgnoreCase(xFor)) {
            xFor = serverHttpRequest.getHeader("Proxy-Client-IP");
        }
        if (StringUtil.isBlank(xFor) || "unknown".equalsIgnoreCase(xFor)) {
            xFor = serverHttpRequest.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtil.isBlank(xFor) || "unknown".equalsIgnoreCase(xFor)) {
            xFor = serverHttpRequest.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtil.isBlank(xFor) || "unknown".equalsIgnoreCase(xFor)) {
            xFor = serverHttpRequest.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtil.isBlank(xFor) || "unknown".equalsIgnoreCase(xFor)) {
            if(serverHttpRequest.getSource() instanceof ServerHttpRequest) {
                xFor = ((ServerHttpRequest)serverHttpRequest.getSource()).getRemoteAddress().getAddress().getHostAddress();
            }
        }
        String ip = "0:0:0:0:0:0:0:1".equals(xFor) ? "127.0.0.1" : xFor;
        return ip;
    }
}
