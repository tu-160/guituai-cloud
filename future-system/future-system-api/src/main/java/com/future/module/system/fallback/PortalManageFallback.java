package com.future.module.system.fallback;

import org.springframework.stereotype.Component;

import com.future.common.base.ActionResult;
import com.future.common.exception.DataException;
import com.future.module.system.PortalManageApi;
import com.future.module.system.entity.PortalManageEntity;
import com.future.module.system.model.portalManage.PortalManagePage;
import com.future.module.system.model.portalManage.PortalManagePageDO;
import com.future.module.system.model.portalManage.PortalManageSelectModel;
import com.future.module.system.model.portalManage.PortalManageVO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 类功能
 *
 * @author Future Platform Group
 * @version v4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2023-02-17
 */
@Component
public class PortalManageFallback implements PortalManageApi {


    @Override
    public List<PortalManageVO> getList(String systemId, String platform){
        return new ArrayList<>();
    }

    @Override
    public List<PortalManageVO> getListByPortalIdAndPlatform(String portalId, String platform) {
        return new ArrayList<>();
    }

    @Override
    public List<PortalManageVO> getListByEnable(String systemId, String platform) {
        return new ArrayList<>();
    }

    @Override
    public List<PortalManageEntity> getAll() {
        return new ArrayList<>();
    }

    @Override
    public List<PortalManageVO> getListByPortalId(String portalId){
        return new ArrayList<>();
    }

    @Override
    public List<PortalManageEntity> listByIdsAndPlatform(List<String> ids, String platform) {
        return new ArrayList<>();
    }

    @Override
    public void createBatch(String platform, String portalId, List<String> systemIdList) {
    }

    @Override
    public List<PortalManagePageDO> getSelectList(PortalManagePage portalManagePage) {
        return new ArrayList<>();
    }

    @Override
    public List<PortalManagePageDO> selectPortalBySystemIds(PortalManageSelectModel model) {
        return new ArrayList<>();
    }

}
