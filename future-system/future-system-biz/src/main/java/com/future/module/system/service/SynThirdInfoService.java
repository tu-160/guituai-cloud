package com.future.module.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.future.base.service.SuperService;
import com.future.module.system.entity.SynThirdInfoEntity;
import com.future.module.system.entity.SysConfigEntity;
import com.future.module.system.model.synthirdinfo.SynThirdTotal;

import java.util.List;

/**
 * 第三方工具的公司-部门-用户同步表模型
 *
 * @@version V4.0.0
 * @@copyright 直方信息科技有限公司
 * @@author Future Platform Group
 * @date 2021/4/23 17:29
 */
public interface SynThirdInfoService extends SuperService<SynThirdInfoEntity> {

    /**
     * 获取指定第三方工具、指定数据类型的数据列表
     * @param thirdType
     * @param dataType
     * @return
     */
    List<SynThirdInfoEntity> getList(String thirdType,String dataType);

    /**
     * 获取同步的详细信息
     * @param id
     * @return
     */
    SynThirdInfoEntity getInfo(String id);

    void create(SynThirdInfoEntity entity);

    boolean update(String id,SynThirdInfoEntity entity);

    void delete(SynThirdInfoEntity entity);

    /**
     * 获取指定第三方工具、指定数据类型、对象ID的同步信息
     * @param thirdType
     * @param dataType
     * @param id
     * @return
     */
    SynThirdInfoEntity getInfoBySysObjId(String thirdType,String dataType,String id);

    /**
     * 获取指定第三方工具、指定数据类型的同步统计信息
     * @param thirdType
     * @param dataType
     * @return
     */
    SynThirdTotal getSynTotal(String thirdType,String dataType);

    /**
     *
     * @param thirdToSysType
     * @param dataTypeOrg
     * @param SysToThirdType
     * @return
     */
    List<SynThirdInfoEntity> syncThirdInfoByType(String thirdToSysType, String dataTypeOrg, String SysToThirdType);

    boolean getBySysObjId(String id);

    String getSysByThird(String valueOf);

    void initBaseDept(Long dingRootDeptId, String access_token, String thirdType);

    /**
     * 获取指定第三方工具、指定数据类型、第三方对象ID的同步信息 20220331
     * @param thirdType
     * @param dataType
     * @param thirdObjId
     * @return
     */
    SynThirdInfoEntity getInfoByThirdObjId(String thirdType,String dataType,String thirdObjId);

}
