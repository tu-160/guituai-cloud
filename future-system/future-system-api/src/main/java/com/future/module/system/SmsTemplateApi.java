package com.future.module.system;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.future.common.base.SmsModel;
import com.future.feign.utils.FeignName;
import com.future.module.system.entity.SmsTemplateEntity;
import com.future.module.system.fallback.SmsTemplateApiFallback;

/**
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date ：2022/4/7 15:31
 */
@FeignClient(name = FeignName.SYSTEM_SERVER_NAME, fallback = SmsTemplateApiFallback.class, path = "/SmsTemplate")
public interface SmsTemplateApi {
    /**
     * 获取短信模板配置
     *
     * @return
     */
    @GetMapping("/getSmsConfig")
    SmsModel getSmsConfig(@RequestParam("tenantId") String tenantId, @RequestParam("dbName") String dbName, @RequestParam("isAssign") boolean isAssign);

    @GetMapping("/getInfoById/{smsId}")
    SmsTemplateEntity getInfoById(@PathVariable("smsId") String smsId, @RequestParam("tenantId") String tenantId, @RequestParam("dbName") String dbName, @RequestParam("isAssign") boolean isAssign);
}
