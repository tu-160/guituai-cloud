package com.future.module.system.fallback;

import org.springframework.stereotype.Component;

import com.future.module.system.ModuleColumnApi;
import com.future.module.system.entity.ModuleColumnEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date ：2022/4/7 14:40
 */
@Component
public class ModuleColumnApiFallback implements ModuleColumnApi {
    @Override
    public List<ModuleColumnEntity> getList() {
        return new ArrayList<>();
    }

    @Override
    public List<ModuleColumnEntity> getListByModuleId(List<String> ids) {
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<ModuleColumnEntity> getListByIds(List<String> ids) {
        return new ArrayList<>();
    }
}
