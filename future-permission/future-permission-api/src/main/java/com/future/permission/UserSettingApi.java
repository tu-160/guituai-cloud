package com.future.permission;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.future.feign.utils.FeignName;
import com.future.permission.fallback.UserApiFallback;
import com.future.permission.fallback.UserSettingApiFallback;
import com.future.permission.model.authorize.AuthorizeVO;

@FeignClient(name = FeignName.PERMISSION_SERVER_NAME, fallback = UserSettingApiFallback.class, path = "/Users/Current")
public interface UserSettingApi {
    /**
     * 查看分级应用权限
     * @return
     */
    @GetMapping("/getAuthorize")
    AuthorizeVO getAuthorize();
}
