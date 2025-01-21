package com.future.permission.model.portalManage;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.future.common.base.MyBatisPrimaryBase;
import com.future.permission.entity.AuthorizeEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 类功能
 *
 * @author Future Platform Group
 * @version v4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2023-04-20
 */
@Data
public class AuthorizePortalManagePrimary extends MyBatisPrimaryBase<AuthorizeEntity> {

    @Schema(description = "权限类型")
    private final String objectType = "role";

    @Schema(description = "条目类型")
    private final String itemType = "portalManage";

    @Schema(description = "角色Id")
    private String roleId;

    @Schema(description = "门户管理Id")
    private String portalManageId;

    public AuthorizePortalManagePrimary(String roleId, String portalManageId){
        this.roleId = roleId;
        this.portalManageId = portalManageId;
    }

    public QueryWrapper<AuthorizeEntity> getQuery(){
        queryWrapper.lambda().eq(AuthorizeEntity::getObjectType, objectType);
        queryWrapper.lambda().eq(AuthorizeEntity::getItemType, itemType);
        if(this.roleId != null) queryWrapper.lambda().eq(AuthorizeEntity::getObjectId, roleId);
        if(this.portalManageId != null) queryWrapper.lambda().eq(AuthorizeEntity::getItemId, portalManageId);
        return queryWrapper;
    }

}
