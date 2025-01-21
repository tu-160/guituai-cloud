package com.future.permission.util.socials;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import me.zhyd.oauth.model.AuthCallback;

/**
 * 流程设计
 *
 * @author Future Platform Group
 * @version v4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2022/7/21 17:17:44
 */
@Data
public class AuthCallbackNew extends AuthCallback {
    private String authCode;
}
