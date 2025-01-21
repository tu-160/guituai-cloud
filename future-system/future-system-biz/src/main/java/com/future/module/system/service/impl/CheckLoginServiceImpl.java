package com.future.module.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.future.base.service.SuperServiceImpl;
import com.future.common.util.type.StringNumber;
import com.future.module.system.entity.EmailConfigEntity;
import com.future.module.system.mapper.CheckLoginMapper;
import com.future.module.system.model.MailAccount;
import com.future.module.system.service.CheckLoginService;
import com.future.module.system.util.EmailCheckUtil;

import org.springframework.stereotype.Service;
/**
 * 邮箱验证业务接口实现类
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-03-23
 */
@Service
public class CheckLoginServiceImpl extends SuperServiceImpl<CheckLoginMapper,EmailConfigEntity> implements CheckLoginService {
    @Override
    public String checkLogin(EmailConfigEntity configEntity) {
        MailAccount mailAccount = new MailAccount();
        mailAccount.setAccount(configEntity.getAccount());
        mailAccount.setPassword(configEntity.getPassword());
        mailAccount.setPop3Host(configEntity.getPop3Host());
        mailAccount.setPop3Port(configEntity.getPop3Port());
        mailAccount.setSmtpHost(configEntity.getSmtpHost());
        mailAccount.setSmtpPort(configEntity.getSmtpPort());
        if (StringNumber.ONE.equals(String.valueOf(configEntity.getEmailSsl()))) {
            mailAccount.setSsl(true);
        } else {
            mailAccount.setSsl(false);
        }
        if (mailAccount.getSmtpHost() != null) {
            return EmailCheckUtil.checkConnected(mailAccount);
        }
        if (mailAccount.getPop3Host() != null) {
            return EmailCheckUtil.checkConnected(mailAccount);
        }
        return "false";
    }
}
