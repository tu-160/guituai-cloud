package com.future.module.system;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.future.feign.utils.FeignName;
import com.future.module.system.entity.MessageTemplateEntity;
import com.future.module.system.fallback.MessageTemplateApiFallback;

/**
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date ：2022/4/7 15:31
 */
@FeignClient(name = FeignName.SYSTEM_SERVER_NAME, fallback = MessageTemplateApiFallback.class, path = "/MessageTemplate")
public interface MessageTemplateApi {
    /**
     * 通过模板id获取消息模板
     *
     * @param templateId
     * @return
     */
    @GetMapping("/getInfoById/{templateId}")
    MessageTemplateEntity getInfoById(@PathVariable("templateId") String templateId);
}
