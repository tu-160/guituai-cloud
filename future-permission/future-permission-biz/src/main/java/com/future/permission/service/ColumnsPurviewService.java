package com.future.permission.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.future.base.service.SuperService;
import com.future.permission.entity.ColumnsPurviewEntity;

import java.util.List;

/**
 * 模块列表权限业务类
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date ：2022/3/15 9:39
 */
public interface ColumnsPurviewService extends SuperService<ColumnsPurviewEntity> {

    /**
     * 通过moduleId获取列表权限
     *
     * @param moduleId
     * @return
     */
    ColumnsPurviewEntity getInfo(String moduleId);

    /**
     * 判断是保存还是编辑
     *
     * @param id
     * @param entity
     * @return
     */
    boolean update(String id, ColumnsPurviewEntity entity);
}
