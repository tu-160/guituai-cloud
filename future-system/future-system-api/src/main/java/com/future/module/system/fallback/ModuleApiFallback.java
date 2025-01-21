package com.future.module.system.fallback;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import com.future.module.system.ModuleApi;
import com.future.module.system.entity.ModuleEntity;
import com.future.module.system.model.module.ModuleApiByIdAndMarkModel;
import com.future.module.system.model.module.ModuleApiByIdsModel;
import com.future.module.system.model.module.ModuleApiModel;
import com.future.module.system.model.online.VisualMenuModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * 调用系统菜单Api降级处理
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-03-24
 */
@Component
public class ModuleApiFallback implements ModuleApi {

    @Override
    public List<ModuleEntity> getList(ModuleApiModel model) {
        return new ArrayList<>();
    }

    @Override
    public List<ModuleEntity> getList(String id) {
        return  new ArrayList<>();
    }

    @Override
    public List<ModuleEntity> getModuleList(String ModuleId) {
        return new ArrayList<>();
    }

    @Override
    public ModuleEntity getModuleByList(String moduleId) {
        return null;
    }

    @Override
    public Integer pubulish(VisualMenuModel visualMenuModel) {
        return 4;
    }

    @Override
    public List<ModuleEntity> getMainModule(@RequestBody ModuleApiModel model) {
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<ModuleEntity> getModuleByIds(@RequestBody ModuleApiByIdsModel model) {
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<ModuleEntity> getModuleBySystemIds(@RequestBody ModuleApiByIdsModel model) {
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<ModuleEntity> getListByEnCode(List<String> enCodeList) {
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<ModuleEntity> getModuleByPortal(List<String> portalIds) {
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<ModuleEntity> findModuleAdmin(ModuleApiByIdAndMarkModel model) {
        return new ArrayList<>();
    }
}
