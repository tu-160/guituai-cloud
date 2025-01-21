package com.future.module.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.future.base.mapper.SuperMapper;
import com.future.module.system.entity.InterfaceOauthEntity;

import org.apache.ibatis.annotations.Mapper;

/**
 * 接口认证
 *
 * @author Future Platform Group
 * @version v4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2022/6/8 9:51
 */
@Mapper
public interface InterfaceOauthMapper extends SuperMapper<InterfaceOauthEntity> {
}
