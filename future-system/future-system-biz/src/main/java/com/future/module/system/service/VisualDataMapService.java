package com.future.module.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.future.base.service.SuperService;
import com.future.common.base.Pagination;
import com.future.module.system.entity.VisualDataMapEntity;

import java.util.List;

/**
 * 大屏地图
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2019年9月26日 上午9:18
 */
public interface VisualDataMapService extends SuperService<VisualDataMapEntity> {

    /**
     * 获取大屏列表(分页)
     *
     * @param pagination 分类
     * @return
     */
    List<VisualDataMapEntity> getList(Pagination pagination);

    /**
     * 获取大屏列表
     *
     * @return
     */
    List<VisualDataMapEntity> getList();

    /**
     * 获取大屏基本信息
     *
     * @param id 主键
     * @return
     */
    VisualDataMapEntity getInfo(String id);

    /**
     * 新增
     *
     * @param entity 实体
     */
    void create(VisualDataMapEntity entity);

    /**
     * 修改
     *
     * @param id     主键
     * @param entity 实体
     * @return
     */
    boolean update(String id, VisualDataMapEntity entity);

    /**
     * 删除
     *
     * @param entity 实体
     */
    void delete(VisualDataMapEntity entity);

    /**
     * 验证名称
     *
     * @param fullName 名称
     * @param id       主键值
     * @return
     */
    boolean isExistByFullName(String fullName, String id);

    /**
     * 验证编码
     *
     * @param enCode 编码
     * @param id     主键值
     * @return
     */
    boolean isExistByEnCode(String enCode, String id);
}
