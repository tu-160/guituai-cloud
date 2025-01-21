package com.future.module.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.future.base.service.SuperServiceImpl;
import com.future.common.base.UserInfo;
import com.future.common.util.RandomUtil;
import com.future.module.system.entity.AdvancedQueryEntity;
import com.future.module.system.mapper.AdvancedQueryMapper;
import com.future.module.system.service.AdvancedQueryService;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 *
 *
 * @author Future Platform Group
 * @version v4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2022/5/30
 */
@Service
public class AdvancedQueryServiceImpl extends SuperServiceImpl<AdvancedQueryMapper, AdvancedQueryEntity> implements AdvancedQueryService {
	@Override
	public void create(AdvancedQueryEntity advancedQueryEntity) {
		String mainId = Optional.ofNullable(advancedQueryEntity.getId()).orElse(RandomUtil.uuId());
		advancedQueryEntity.setId(mainId);
		this.save(advancedQueryEntity);
	}

	@Override
	public AdvancedQueryEntity getInfo(String id,String userId) {
		QueryWrapper<AdvancedQueryEntity> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(AdvancedQueryEntity::getId, id).eq(AdvancedQueryEntity::getCreatorUserId, userId);
		return this.getOne(queryWrapper);
	}

	@Override
	public List<AdvancedQueryEntity> getList(String moduleId, UserInfo userInfo) {
		QueryWrapper<AdvancedQueryEntity> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(AdvancedQueryEntity::getModuleId, moduleId).eq(AdvancedQueryEntity::getCreatorUserId, userInfo.getUserId());
		List<AdvancedQueryEntity> list = this.list(queryWrapper);
		return list;
	}

}
