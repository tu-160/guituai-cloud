package com.future.provider.permission;

import com.future.permission.entity.PositionEntity;

/**
 * 岗位
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-04-30
 */
public interface PositionProvider {
    /**
     * 信息
     *
     * @param id 主键值
     * @return
     */
    PositionEntity getInfo(String id);
}
