package com.future.module.system.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.future.base.service.SuperService;
import com.future.module.system.entity.PortalManageEntity;
import com.future.module.system.model.portalManage.PortalManagePage;
import com.future.module.system.model.portalManage.PortalManagePageDO;
import com.future.module.system.model.portalManage.PortalManagePrimary;
import com.future.module.system.model.portalManage.PortalManageVO;
import com.future.visualdev.portal.model.PortalPagination;

import java.util.List;

/**
 * <p>
 * 门户管理 服务类
 * </p>
 *
 * @author YanYu
 * @since 2023-02-16
 */
public interface PortalManageService extends SuperService<PortalManageEntity> {

    void checkCreUp(PortalManageEntity portalManageEntity) throws Exception;

    PortalManageVO convertVO(PortalManageEntity entity);

    List<PortalManageVO> getList(PortalManagePrimary primary);

    PageDTO<PortalManagePageDO> getPage(PortalManagePage portalPagination);

    List<PortalManagePageDO> getSelectList(PortalManagePage pmPage);

    List<PortalManagePageDO> selectPortalBySystemIds(List<String> systemIds, List<String> collect);

    void createBatch(List<PortalManagePrimary> primaryLit) throws Exception;

}
