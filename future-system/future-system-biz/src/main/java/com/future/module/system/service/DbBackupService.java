package com.future.module.system.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.future.common.base.Pagination;
import com.future.module.system.entity.DbBackupEntity;

/**
 * 数据备份
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2019年9月27日 上午9:18
 */
public interface DbBackupService extends IService<DbBackupEntity> {

    /**
     * 列表
     *
     * @param pagination 条件
     * @return
     */
    List<DbBackupEntity> getList(Pagination pagination);

    /**
     * 信息
     *
     * @param id 主键值
     * @return
     */
    DbBackupEntity getInfo(String id);

    /**
     * 删除
     *
     * @param entity 实体对象
     */
    void delete(DbBackupEntity entity);

    /**
     * 创建
     *
     * @param entity 实体对象
     */
    void create(DbBackupEntity entity);

    /**
     * 备份
     * @return
     */
    boolean dbBackup();
}
