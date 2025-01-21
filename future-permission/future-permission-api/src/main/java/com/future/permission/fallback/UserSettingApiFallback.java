package com.future.permission.fallback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import com.future.common.base.ActionResult;
import com.future.common.exception.DataException;
import com.future.permission.UserApi;
import com.future.permission.UserSettingApi;
import com.future.permission.entity.UserEntity;
import com.future.permission.model.authorize.AuthorizeVO;
import com.future.permission.model.user.*;

import java.util.*;

/**
 * 获取用户信息Api降级处理
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-03-24
 */
@Component
@Slf4j
public class UserSettingApiFallback implements UserSettingApi {

    @Override
    public AuthorizeVO getAuthorize() {
        return null;
    }
}
