package com.future.module.system.fallback;

import org.springframework.stereotype.Component;

import com.future.module.system.ModuleDataAuthorizeSchemeApi;
import com.future.module.system.entity.ModuleDataAuthorizeSchemeEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date ：2022/4/7 14:43
 */
@Component
public class ModuleDataAuthorizeSchemeApiFallback implements ModuleDataAuthorizeSchemeApi {
    @Override
    public List<ModuleDataAuthorizeSchemeEntity> getList() {
        return new ArrayList<>();
    }

    @Override
    public List<ModuleDataAuthorizeSchemeEntity> getListByModuleId(List<String> ids) {
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<ModuleDataAuthorizeSchemeEntity> getListByIds(List<String> ids) {
        return new ArrayList<>();
    }
}
