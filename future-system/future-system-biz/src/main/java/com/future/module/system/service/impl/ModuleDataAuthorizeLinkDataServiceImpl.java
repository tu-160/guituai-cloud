package com.future.module.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.future.base.service.SuperServiceImpl;
import com.future.module.system.entity.ModuleDataAuthorizeLinkEntity;
import com.future.module.system.mapper.ModuleDataAuthorizeLinkDataMapper;
import com.future.module.system.service.ModuleDataAuthorizeLinkDataService;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 数据权限方案
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2019年9月27日 上午9:18
 */
@Service
public class ModuleDataAuthorizeLinkDataServiceImpl extends SuperServiceImpl<ModuleDataAuthorizeLinkDataMapper, ModuleDataAuthorizeLinkEntity> implements ModuleDataAuthorizeLinkDataService {


	@Override
	public ModuleDataAuthorizeLinkEntity getLinkDataEntityByMenuId(String menuId,Integer type) {
		QueryWrapper<ModuleDataAuthorizeLinkEntity> linkEntityQueryWrapper = new QueryWrapper<>();
		linkEntityQueryWrapper.lambda().eq(ModuleDataAuthorizeLinkEntity::getModuleId,menuId).eq(ModuleDataAuthorizeLinkEntity::getDataType,type);
		List<ModuleDataAuthorizeLinkEntity> list = this.list(linkEntityQueryWrapper);
		if (list.size()>0){
			return list.get(0);
		}
		return new ModuleDataAuthorizeLinkEntity();
	}
}
