package com.future.permission;

import com.alibaba.fastjson.JSONObject;
import com.future.common.exception.LoginException;
import com.future.feign.utils.FeignName;
import com.future.permission.entity.SocialsUserEntity;
import com.future.permission.fallback.SocialsUserApiFallback;
import com.future.permission.model.socails.SocialsUserInfo;
import com.future.permission.model.socails.SocialsUserVo;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 获取分组信息Api
 *
 * @author Future Platform Group
 * @version v4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2022/8/19
 */
@FeignClient(name = FeignName.PERMISSION_SERVER_NAME, fallback = SocialsUserApiFallback.class, path = "/socials")
public interface SocialsUserApi {

    @GetMapping("/list")
    List<SocialsUserVo> getLoginList(@RequestParam("ticket") String ticket);

    @GetMapping("/callback")
    JSONObject binding(@RequestParam("source") String source,
                       @RequestParam(value = "userId", required = false) String userId,
                       @RequestParam(value = "tenantId", required = false) String tenantId,
                       @RequestParam(value = "code", required = false) String code,
                       @RequestParam(value = "state", required = false) String state );

    @GetMapping("/getSocialsUserInfo")
    SocialsUserInfo getSocialsUserInfo(@RequestParam("source") String source, @RequestParam("code") String code, @RequestParam(value = "state", required = false) String state) throws LoginException;

    @GetMapping("/getUserInfo")
    SocialsUserInfo getUserInfo(@RequestParam("source") String source, @RequestParam("uuid") String uuid, @RequestParam(value = "socialName", required = false) String socialName) throws LoginException;

    @GetMapping("/loginbind")
    void loginAutoBinding(@RequestParam("socialType") String socialType,
                          @RequestParam("socialUnionid") String socialUnionid,
                          @RequestParam("socialName") String socialName,
                          @RequestParam("userId") String userId,
                          @RequestParam(value = "tenantId", required = false) String tenantId );

    @GetMapping("/getInfoBySocialId")
    SocialsUserEntity getInfoBySocialId(@RequestParam("socialId") String socialId, @RequestParam("socialType") String socialType);
}
