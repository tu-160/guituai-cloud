package com.future.permission.mapper;


import org.apache.ibatis.annotations.Param;

import com.future.base.mapper.SuperMapper;
import com.future.module.system.model.base.SystemBaeModel;
import com.future.module.system.model.button.ButtonModel;
import com.future.module.system.model.column.ColumnModel;
import com.future.module.system.model.form.ModuleFormModel;
import com.future.module.system.model.module.ModuleModel;
import com.future.module.system.model.resource.ResourceModel;
import com.future.permission.entity.AuthorizeEntity;

import java.util.List;

/**
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021/3/12 15:27
 */
public interface AuthorizeMapper extends SuperMapper<AuthorizeEntity> {


    List<ModuleModel> findModule(@Param("objectId") String objectId, @Param("id") String id, @Param("moduleAuthorize") List<String> moduleAuthorize, @Param("moduleUrlAddressAuthorize") List<String> moduleUrlAddressAuthorize, @Param("mark") Integer mark);

    List<ButtonModel> findButton(@Param("objectId") String objectId);

    List<ColumnModel> findColumn(@Param("objectId") String objectId);

    List<ResourceModel> findResource(@Param("objectId") String objectId);

    List<ModuleFormModel> findForms(@Param("objectId") String objectId);

    List<SystemBaeModel> findSystem(@Param("objectId") String objectId, @Param("enCode") String enCode, @Param("moduleAuthorize") List<String> moduleAuthorize, @Param("mark") Integer mark);

    List<ButtonModel> findButtonAdmin(@Param("mark") Integer mark);

    List<ColumnModel> findColumnAdmin(@Param("mark") Integer mark);

    List<ResourceModel> findResourceAdmin(@Param("mark") Integer mark);

    List<ModuleFormModel> findFormsAdmin(@Param("mark") Integer mark);

    void saveBatch(@Param("values") String values);

    void savaAuth(AuthorizeEntity authorizeEntity);

}
