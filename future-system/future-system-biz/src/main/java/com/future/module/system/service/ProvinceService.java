package com.future.module.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.future.base.service.SuperService;
import com.future.common.base.Page;
import com.future.module.system.entity.ProvinceEntity;
import com.future.module.system.model.province.PaginationProvince;

import java.util.List;

/**
 * 行政区划
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2019年9月27日 上午9:18
 */
public interface ProvinceService extends SuperService<ProvinceEntity> {


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

    /**
     * 普通列表
     *
     * @param parentId 节点Id
     * @return
     */
    List<ProvinceEntity> getList(String parentId);

    /**
     * 普通列表
     *
     * @param parentId 节点Id
     * @param page
     * @return ignore
     */
    List<ProvinceEntity> getList(String parentId, PaginationProvince page);

    /**
     * 普通列表
     *
     * @return
     */
    List<ProvinceEntity> getAllList();

    /**
     * 地域名列表（在线开发）
     *
     * @return ignore
     */
    List<ProvinceEntity> getAllProList();

    List<ProvinceEntity> getProListBytype(String type);

    /**
     * 省市区单条数据集合(代码生成器)
     * @param ProIdList 省市区id集合
     * @return
     */
    List<ProvinceEntity> getProList(List<String> ProIdList);

    /**
     * 信息
     *
     * @param id 主键值
     * @return
     */
    ProvinceEntity getInfo(String id);

    /**
     * 信息
     *
     * @param fullName
     * @param parentId
     * @return ignore
     */
    ProvinceEntity getInfo(String fullName,List<String> parentId);

    /**
     * 删除
     *
     * @param entity 实体对象
     */
    void delete(ProvinceEntity entity);

    /**
     * 创建
     *
     * @param entity 实体对象
     */
    void create(ProvinceEntity entity);

    /**
     * 更新
     *
     * @param id     主键值
     * @param entity 实体对象
     * @return
     */
    boolean update(String id, ProvinceEntity entity);

    /**
     * 上移
     *
     * @param id 主键值
     * @return
     */
    boolean first(String id);

    /**
     * 下移
     *
     * @param id 主键值
     * @return
     */
    boolean next(String id);

    /**
     * 数组
     */
    List<ProvinceEntity> infoList(List<String> list);
}
