package com.future.module.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.future.base.service.SuperService;
import com.future.module.system.entity.ModuleDataAuthorizeLinkEntity;


/**
 * 数据权限配置
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2019年9月27日 上午9:18
 */
public interface ModuleDataAuthorizeLinkDataService extends SuperService<ModuleDataAuthorizeLinkEntity> {
	/**
	 * 根据菜单id获取数据连接
	 * @param menuId
	 * @return
	 */
	ModuleDataAuthorizeLinkEntity getLinkDataEntityByMenuId(String menuId,Integer type);

}
