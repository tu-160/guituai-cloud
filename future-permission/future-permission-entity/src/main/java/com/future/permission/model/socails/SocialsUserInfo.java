package com.future.permission.model.socails;

import com.alibaba.fastjson.JSONArray;
import com.future.common.base.UserInfo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 流程设计
 *
 * @author Future Platform Group
 * @version v4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2022/9/8 11:33:59
 */
@Data
public class SocialsUserInfo {
    UserInfo userInfo;
    JSONArray tenantUserInfo;
    String socialUnionid;
    String socialName;
}
