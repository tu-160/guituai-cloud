package com.future.module.system;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.future.common.base.ActionResult;
import com.future.common.base.Page;
import com.future.feign.utils.FeignName;
import com.future.module.system.fallback.UserOnlineApiFallback;
import com.future.module.system.model.UserOnlineModel;

import java.util.List;
/**
 * 调用在线用户Api
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-03-24
 */
@FeignClient(name = FeignName.SYSTEM_SERVER_NAME , fallback = UserOnlineApiFallback.class, path = "/Permission/OnlineUser")
public interface UserOnlineApi {

}
