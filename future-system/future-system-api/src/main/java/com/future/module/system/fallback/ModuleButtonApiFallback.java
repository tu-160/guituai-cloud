package com.future.module.system.fallback;

import org.springframework.stereotype.Component;

import com.future.module.system.ModuleButtonApi;
import com.future.module.system.entity.ModuleButtonEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date ：2022/4/7 14:38
 */
@Component
public class ModuleButtonApiFallback implements ModuleButtonApi {
    @Override
    public List<ModuleButtonEntity> getList() {
        return new ArrayList<>();
    }

    @Override
    public List<ModuleButtonEntity> getListByModuleIds(List<String> ids) {
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<ModuleButtonEntity> getListByIds(List<String> ids) {
        return new ArrayList<>();
    }
}
