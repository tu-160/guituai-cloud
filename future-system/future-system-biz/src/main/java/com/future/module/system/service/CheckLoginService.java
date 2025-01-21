package com.future.module.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.future.base.service.SuperService;
import com.future.module.system.entity.EmailConfigEntity;
/**
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-03-23
 */
public interface CheckLoginService extends SuperService<EmailConfigEntity> {
    /**
     * 邮箱验证
     *
     * @param configEntity
     * @return
     */
    String checkLogin(EmailConfigEntity configEntity);
}
