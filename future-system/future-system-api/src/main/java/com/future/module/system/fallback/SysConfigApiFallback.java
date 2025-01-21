package com.future.module.system.fallback;

import org.springframework.stereotype.Component;

import com.future.common.model.BaseSystemInfo;
import com.future.module.system.SysConfigApi;
import com.future.module.system.entity.SysConfigEntity;

import java.util.List;

/**
 * 调用系统配置Api降级处理
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-03-24
 */
@Component
public class SysConfigApiFallback implements SysConfigApi {

    @Override
    public BaseSystemInfo getSysInfo(String tenantId) {
        return new BaseSystemInfo();
    }

    @Override
    public BaseSystemInfo getSysConfigInfo() {
        return new BaseSystemInfo();
    }

    @Override
    public String getValueByKey(String keyStr) {
        return null;
    }

    @Override
    public List<SysConfigEntity> getSysConfigInfoByType(String type, String tenantId) {
        return null;
    }
}
