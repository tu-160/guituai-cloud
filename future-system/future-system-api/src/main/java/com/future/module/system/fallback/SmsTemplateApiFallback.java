package com.future.module.system.fallback;

import org.springframework.stereotype.Component;

import com.future.common.base.SmsModel;
import com.future.module.system.SmsTemplateApi;
import com.future.module.system.entity.SmsTemplateEntity;

/**
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date ：2022/4/7 15:36
 */
@Component
public class SmsTemplateApiFallback implements SmsTemplateApi {
    @Override
    public SmsModel getSmsConfig(String tenantId, String dbName, boolean isAssign) {
        return null;
    }

    @Override
    public SmsTemplateEntity getInfoById(String smsId, String tenantId, String dbName, boolean isAssign) {
        return null;
    }
}
