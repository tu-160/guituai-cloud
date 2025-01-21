package com.future.provider.system;

import java.util.List;

import com.future.module.system.entity.ModuleButtonEntity;

/**
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-05-11
 */
public interface ModuleButtonProvider {

    /**
     * 列表
     *
     * @return
     */
    List<ModuleButtonEntity> getList();

}
