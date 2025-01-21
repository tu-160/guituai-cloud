package com.future.module.system.service;

import java.util.List;

import com.future.base.service.SuperService;
import com.future.module.system.entity.CommonWordsEntity;
import com.future.module.system.model.commonword.ComWordsPagination;

/**
 * 审批常用语 Service
 *
 * @author Future Platform Group
 * @version v4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2023-01-06
 */
public interface CommonWordsService extends SuperService<CommonWordsEntity> {

    /**
     * 系统常用语列表
     *
     * @param comWordsPagination 页面对象
     * @return 打印实体类
     */
    List<CommonWordsEntity> getSysList(ComWordsPagination comWordsPagination, Boolean currentSysFlag);

    /**
     *  个人常用语列表
     *
     * @param type 类型
     * @return 集合
     */
    List<CommonWordsEntity> getListModel(String type);

    /**
     * 系统是否被使用
     * @param systemId 系统ID
     * @return 返回判断
     */
    Boolean existSystem(String systemId);

}
