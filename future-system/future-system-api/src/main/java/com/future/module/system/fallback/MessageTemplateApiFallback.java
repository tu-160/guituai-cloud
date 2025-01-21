package com.future.module.system.fallback;

import org.springframework.stereotype.Component;

import com.future.module.system.MessageTemplateApi;
import com.future.module.system.entity.MessageTemplateEntity;

/**
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date ：2022/4/7 15:34
 */
@Component
public class MessageTemplateApiFallback implements MessageTemplateApi {
    @Override
    public MessageTemplateEntity getInfoById(String templateId) {
        return null;
    }
}
