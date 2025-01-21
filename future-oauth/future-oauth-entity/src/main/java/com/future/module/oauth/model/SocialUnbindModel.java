package com.future.module.oauth.model;

import lombok.AllArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 第三方未绑定模型
 *
 * @author Future Platform Group
 * @version v4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2022/9/19 15:06:31
 */
@Data
@AllArgsConstructor
public class SocialUnbindModel {
    String socialType;
    String socialUnionid;
    String socialName;
}
