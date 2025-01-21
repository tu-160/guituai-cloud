package com.future.module.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.future.base.mapper.SuperMapper;
import com.future.module.system.entity.DataInterfaceEntity;

import org.apache.ibatis.annotations.Mapper;

/**
 * 数据接口
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-03-23
 */
@Mapper
public interface DataInterfaceMapper extends SuperMapper<DataInterfaceEntity> {

}
