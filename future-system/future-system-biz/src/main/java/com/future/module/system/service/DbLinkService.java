package com.future.module.system.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.future.base.service.SuperService;
import com.future.common.exception.DataException;
import com.future.database.model.dto.DbConnDTO;
import com.future.database.model.entity.DbLinkEntity;
import com.future.module.system.model.dblink.PaginationDbLink;

import java.util.List;

/**
 * 数据连接
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2019年9月27日 上午9:18
 */
public interface DbLinkService extends SuperService<DbLinkEntity> {

    /**
     * 列表
     *
     * @return ignore
     */
    List<DbLinkEntity> getList();

    /**
     * 列表关键字查询
     *
     * @param pagination 数据连接分页
     * @return ignore
     */
    List<DbLinkEntity> getList(PaginationDbLink pagination);

    /**
     * 信息
     *
     * @param id 主键
     * @return ignore
     */
    DbLinkEntity getInfo(String id);

    /**
     * 验证名称
     *
     * @param fullName 名称
     * @param id       主键值
     * @return ignore
     */
    boolean isExistByFullName(String fullName, String id);

    DbLinkEntity getInfoByFullName(String fullName);

    /**
     * 创建
     *
     * @param entity 实体对象
     */
    void create(DbLinkEntity entity);

    /**
     * 更新
     *
     * @param id     主键值
     * @param entity 实体对象
     * @return ignore
     */
    boolean update(String id, DbLinkEntity entity);

    /**
     * 删除
     *
     * @param entity 实体对象
     */
    void delete(DbLinkEntity entity);

    /**
     * 上移
     *
     * @param id 主键值
     * @return ignore
     */
    boolean first(String id);

    /**
     * 下移
     *
     * @param id 主键值
     * @return ignore
     */
    boolean next(String id);

    /**
     * 测试连接
     *
     * @param entity 实体对象
     * @return ignore
     * @throws DataException ignore
     */
    boolean testDbConnection(DbLinkEntity entity) throws Exception;

    /**
     * 获取动态数据源
     *
     * @param dbLinkId 数据连接ID
     * @return 动态数据库源
     * @throws DataException ignore
     */
    DbLinkEntity getResource(String dbLinkId) throws Exception;

}
