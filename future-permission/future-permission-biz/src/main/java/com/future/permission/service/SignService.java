package com.future.permission.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.future.base.service.SuperService;
import com.future.permission.entity.SignEntity;

import java.util.List;

/**
 * 个人签名
 *
 * @author Future Platform Group
 * @copyright 直方信息科技有限公司
 * @date 2022年9月2日 上午9:18
 */
public interface SignService extends SuperService<SignEntity> {


    /**
     * 列表
     *
     * @return 个人签名集合
     */
    List<SignEntity> getList();





    /**
     * 创建
     *
     * @param entity 实体对象
     */
    boolean create(SignEntity entity);



    /**
     * 删除
     *
     * @param entity 实体对象
     */
    boolean delete(String id);


    boolean  updateDefault(String id);


    //获取默认
    SignEntity  getDefaultByUserId(String id);
}
