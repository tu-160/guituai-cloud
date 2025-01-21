package com.future.provider.system;

import com.future.common.model.BaseSystemInfo;

/**
 * 使用RPC获取系统配置
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-04-29
 */
public interface SysConfigProvider {
    /**
     * 获取系统配置
     * @return
     */
    BaseSystemInfo getSysInfo();

}
