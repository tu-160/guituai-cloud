package com.future.module.system.fallback;

import org.springframework.stereotype.Component;

import com.future.common.base.ActionResult;
import com.future.common.base.Page;
import com.future.module.system.UserOnlineApi;
import com.future.module.system.model.UserOnlineModel;

import java.util.List;
/**
 * 调用在线用户Api降级处理
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-03-24
 */
@Component
public class UserOnlineApiFallback implements UserOnlineApi {

}
