package com.future.module.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.future.base.service.SuperService;
import com.future.module.system.entity.ProvinceAtlasEntity;

import java.util.List;

/**
 * 行政区划
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2019年9月27日 上午9:18
 */
public interface ProvinceAtlasService extends SuperService<ProvinceAtlasEntity> {

    List<ProvinceAtlasEntity> getList();

    List<ProvinceAtlasEntity> getListByPid(String pid);

    ProvinceAtlasEntity findOneByCode(String code);
}
