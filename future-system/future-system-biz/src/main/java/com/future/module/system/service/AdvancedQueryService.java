package com.future.module.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.future.base.service.SuperService;
import com.future.common.base.UserInfo;
import com.future.module.system.entity.AdvancedQueryEntity;

import java.util.List;

/**
 *
 *
 * @author Future Platform Group
 * @version v4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2022/5/30
 */

public interface AdvancedQueryService extends SuperService<AdvancedQueryEntity> {

	void create(AdvancedQueryEntity advancedQueryEntity);

	AdvancedQueryEntity getInfo(String id,String userId);

	List<AdvancedQueryEntity> getList(String moduleId, UserInfo userInfo);
}
