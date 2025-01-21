package com.future.provider.system;

import java.util.List;

import com.future.module.system.entity.ModuleDataAuthorizeSchemeEntity;

/**
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-05-11
 */
public interface ModuleDataAuthorizeSchemeProvider {

    /**
     * 列表
     *
     * @return
     */
    List<ModuleDataAuthorizeSchemeEntity> getList();

}
