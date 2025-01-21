package com.future.provider.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.future.common.base.UserInfo;
import com.future.common.util.UserProvider;
import com.future.database.util.TenantDataSourceUtil;
import com.future.module.system.entity.LogEntity;
import com.future.module.system.service.LogService;
import com.future.provider.system.LogProvider;
import com.future.reids.config.ConfigValueUtil;


/**
 * Dubbo服务提供者
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-07-27
 */
@Slf4j
@DubboService
public class LogProviderImpl implements LogProvider {

    @Autowired
    private ConfigValueUtil configValueUtil;
    @Autowired
    private LogService logService;

    @Override
    public void writeLogAsync(String dbId, String userId, String userName, String account, String abstracts) {
        //判断是否为多租户
        if (configValueUtil.isMultiTenancy()) {
            TenantDataSourceUtil.switchTenant(dbId);
        }
        try {
            if(log.isDebugEnabled()) {
                log.debug("写入登录日志：" + userName);
            }
//            logService.writeLogAsync(userInfo.getUserId(), userInfo.getUserName() + "/" + userInfo.getUserAccount(), "登录成功", (System.currentTimeMillis() - millis));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void writeLogRequest(LogEntity logEntity) {
        UserInfo userInfo = UserProvider.getUser();
        if (configValueUtil.isMultiTenancy()) {
            TenantDataSourceUtil.switchTenant(userInfo.getTenantId());
        }
        try {
            if(log.isDebugEnabled()) {
                log.debug("写入日志：" + logEntity);
            }
            logService.save(logEntity);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}
