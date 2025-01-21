package com.future.provider.system.mock;

import com.future.common.model.BaseSystemInfo;
import com.future.provider.system.SysConfigProvider;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-05-12
 */
@Slf4j
public class SysConfigProviderMock implements SysConfigProvider {
    @Override
    public BaseSystemInfo getSysInfo() {
        log.error("获取系统信息接口调用失败");
        return null;
    }
}
