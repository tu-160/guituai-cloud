package com.future.module.system.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.future.base.mapper.SuperMapper;
import com.future.module.system.entity.DictionaryDataEntity;

import org.apache.ibatis.annotations.Param;

/**
 * 字典数据
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2019年9月27日 上午9:18
 */
public interface DictionaryDataMapper extends SuperMapper<DictionaryDataEntity> {

    DictionaryDataEntity getByTypeDataCode(@Param("typeCode") String typeCode, @Param("dataCode") String dataCode);

}
