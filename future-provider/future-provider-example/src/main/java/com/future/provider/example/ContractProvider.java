package com.future.provider.example;

import com.future.module.example.entity.ContractEntity;

/**
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-06-21
 */
public interface ContractProvider {

    /**
     * 获取详情
     * @param id
     * @return
     */
    ContractEntity getInfo(String id);

}
