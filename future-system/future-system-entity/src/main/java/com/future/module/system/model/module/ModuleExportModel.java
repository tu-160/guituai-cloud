package com.future.module.system.model.module;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

import com.future.common.base.entity.*;
import com.future.module.system.entity.ModuleButtonEntity;
import com.future.module.system.entity.ModuleColumnEntity;
import com.future.module.system.entity.ModuleDataAuthorizeEntity;
import com.future.module.system.entity.ModuleDataAuthorizeSchemeEntity;
import com.future.module.system.entity.ModuleEntity;
import com.future.module.system.entity.ModuleFormEntity;

/**
 * 系统菜单导出模型
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-06-17
 */
@Data
public class ModuleExportModel {
    private ModuleEntity moduleEntity;

    private List<ModuleButtonEntity> buttonEntityList;
    private List<ModuleColumnEntity> columnEntityList;
    private List<ModuleFormEntity> formEntityList;
    private List<ModuleDataAuthorizeSchemeEntity> schemeEntityList;
    private List<ModuleDataAuthorizeEntity> authorizeEntityList;
}
