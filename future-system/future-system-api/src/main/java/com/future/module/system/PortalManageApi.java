package com.future.module.system;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import com.future.feign.utils.FeignName;
import com.future.module.system.entity.PortalManageEntity;
import com.future.module.system.fallback.PortalManageFallback;
import com.future.module.system.model.portalManage.PortalManagePage;
import com.future.module.system.model.portalManage.PortalManagePageDO;
import com.future.module.system.model.portalManage.PortalManageSelectModel;
import com.future.module.system.model.portalManage.PortalManageVO;

import java.util.List;
import java.util.Map;

@FeignClient(name = FeignName.SYSTEM_SERVER_NAME , fallback = PortalManageFallback.class, path = "/PortalManage")
public interface PortalManageApi {

    @GetMapping("/getList")
    List<PortalManageVO> getList(@RequestParam("systemId") String systemId, @RequestParam("platform") String platform);

    @GetMapping("/getListByPortalIdAndPlatform")
    List<PortalManageVO> getListByPortalIdAndPlatform(@RequestParam("portalId") String portalId, @RequestParam("platform") String platform);

    @GetMapping("/getListByEnable")
    List<PortalManageVO> getListByEnable(@RequestParam("systemId") String systemId, @RequestParam("platform") String platform);

    @GetMapping("/getAll")
    List<PortalManageEntity> getAll();

    @GetMapping("/listByPortalId")
    List<PortalManageVO> getListByPortalId(@RequestParam("portalId") String portalId);

    @PostMapping("/listByIdsAndPlatform")
    List<PortalManageEntity> listByIdsAndPlatform(@RequestBody List<String> ids, @RequestParam("platform") String platform);

    @PostMapping("/createBatch")
    void createBatch(@RequestParam("platform")String platform, @RequestParam("portalId")String portalId,
                     @RequestBody List<String> systemIdList) ;

    @PostMapping("/getSelectList")
    List<PortalManagePageDO> getSelectList(@RequestBody PortalManagePage portalManagePage);

    @PostMapping("/selectPortalBySystemIds")
    List<PortalManagePageDO> selectPortalBySystemIds(@RequestBody PortalManageSelectModel model);
}
