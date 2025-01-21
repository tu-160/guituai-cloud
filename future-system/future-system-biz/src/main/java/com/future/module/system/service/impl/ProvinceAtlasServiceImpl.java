package com.future.module.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.future.base.service.SuperServiceImpl;
import com.future.common.util.StringUtil;
import com.future.module.system.entity.ProvinceAtlasEntity;
import com.future.module.system.entity.ProvinceEntity;
import com.future.module.system.mapper.ProvinceAtlasMapper;
import com.future.module.system.service.ProvinceAtlasService;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 行政区划
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2019年9月27日 上午9:18
 */
@Service
public class ProvinceAtlasServiceImpl extends SuperServiceImpl<ProvinceAtlasMapper, ProvinceAtlasEntity> implements ProvinceAtlasService {

    @Override
    public List<ProvinceAtlasEntity> getList() {
        QueryWrapper<ProvinceAtlasEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ProvinceAtlasEntity::getEnabledMark, 1);
        return  this.list(queryWrapper);
    }

    @Override
    public List<ProvinceAtlasEntity> getListByPid(String pid) {
        QueryWrapper<ProvinceAtlasEntity> queryWrapper = new QueryWrapper<>();
        if (StringUtil.isNotEmpty(pid)) {
            queryWrapper.lambda().eq(ProvinceAtlasEntity::getParentId, pid);
        }else{
            queryWrapper.lambda().eq(ProvinceAtlasEntity::getParentId, "-1");
        }
        queryWrapper.lambda().eq(ProvinceAtlasEntity::getEnabledMark, 1);
        return  this.list(queryWrapper);
    }

    @Override
    public ProvinceAtlasEntity findOneByCode(String code) {
        QueryWrapper<ProvinceAtlasEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ProvinceAtlasEntity::getEnCode, code);
        queryWrapper.lambda().eq(ProvinceAtlasEntity::getEnabledMark, 1);
        return  this.getOne(queryWrapper);
    }
}
