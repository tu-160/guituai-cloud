package com.future.module.system.fallback;

import org.springframework.stereotype.Component;

import com.future.module.system.ModuleFormApi;
import com.future.module.system.entity.ModuleFormEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date ：2022/4/7 14:41
 */
@Component
public class ModuleFormApiFallback implements ModuleFormApi {
    @Override
    public List<ModuleFormEntity> getList() {
        return new ArrayList<>();
    }

    @Override
    public List<ModuleFormEntity> getListByModuleId(List<String> ids) {
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<ModuleFormEntity> getListByIds(List<String> ids)  {
        return new ArrayList<>();
    }
}
